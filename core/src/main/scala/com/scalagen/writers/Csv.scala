package com.scalagen.writers

import java.nio.ByteBuffer
import java.nio.channels._
import java.nio.file.StandardOpenOption._
import java.nio.file.{Path, Paths}
import java.util.concurrent.atomic.{AtomicInteger, AtomicLong}

import com.scalagen.data.api._
import com.scalagen.util.Tabulator

import org.slf4j.{Logger, LoggerFactory}

/**
  * A writer that writes a CSV file based on options provided
  *
  * @param sources - Sources for writing lines to the file
  */
case class Csv(sources: SourceContainer) extends Writer with Headers[Csv] {

  private val logger: Logger      = LoggerFactory.getLogger(getClass)
  private var delimiter: String   = "|"
  private var quoteChar: String   = "\""
  private var quoted: Boolean     = true
  private var newLineChar: String = System.lineSeparator()

  /**
    * What delimiter to use for lines, default is |
    *
    * @param d new delimiters to use
    * @return object with attribute set
    */
  def withDelim(d: String): Csv = {
    delimiter = d
    this
  }

  /**
    * What character to use for quoting values, default is "
    *
    * @param q new quote character to use
    * @return object with attribute set
    */
  def withQuoteChar(q: String): Csv = {
    quoteChar = q
    this
  }

  def withQuoteChar(q: Char): Csv = {
    withQuoteChar(q.toString)
  }

  /**
    * Whether or not to quote values when writing them, default is true
    *
    * @param q new value to use
    * @return object with attribute set
    */
  def withQuoted(q: Boolean): Csv = {
    quoted = q
    this
  }

  /**
    * What new line character to use for separating lines, default is system new line
    *
    * @param n new line value to use
    * @return object with attribute set
    */
  def withNewLine(n: String): Csv = {
    newLineChar = n
    this
  }

  def makeLine: Row = sources.getLine

  def makeCsvLine: String = {
    val line: StringBuilder = StringBuilder.newBuilder
    val data: Row           = makeLine
    val x: Int              = data.length
    var i: Int              = 0
    while (i != x) {
      if (quoted) {
        line.append(quoteChar)
        line.append(data.getString(i))
        line.append(quoteChar)
        if (i + 1 != x) line.append(delimiter)
      } else {
        line.append(data.getString(i))
        if (i + 1 != x) line.append(delimiter)
      }
      i += 1
    }
    line.append(newLineChar)
    line.result()
  }

  def hasHeader = true

  def write(fileName: String, lines: Int): Unit = {
    logger.info(s"Writing $lines lines to $fileName")

    val filePath: Path                       = Paths.get(fileName)
    val fileChannel: AsynchronousFileChannel = AsynchronousFileChannel.open(filePath, TRUNCATE_EXISTING, WRITE, CREATE)
    val filePosition: AtomicLong             = new AtomicLong(0L)
    val i: AtomicInteger                     = new AtomicInteger(1)
    if (hasHeader) {
      val head: String          = headers.mkString(delimiter) + newLineChar
      val headBytes: ByteBuffer = ByteBuffer.wrap(head.getBytes)
      fileChannel.write(headBytes, filePosition.getAndAdd(headBytes.capacity.toLong))
      i.incrementAndGet()
    }
    while (i.get() <= lines) {
      val line: String       = makeCsvLine
      val buffer: ByteBuffer = ByteBuffer.wrap(line.getBytes)
      val lock: FileLock     = fileChannel.lock().get()
      if (lock.isValid) {
        fileChannel.write(buffer, filePosition.getAndAdd(buffer.capacity.toLong), null, new WriteCompletionHandler(fileName))
        i.getAndIncrement()
        lock.release()
      }
    }
    val lastLine: String   = makeCsvLine.stripSuffix(newLineChar)
    val buffer: ByteBuffer = ByteBuffer.wrap(lastLine.getBytes)
    fileChannel.write(buffer, filePosition.get)
    fileChannel.close()
  }

  def show(n: Int = 10): Unit = Tabulator.print(headers, Seq.fill(n)(makeLine.values.map(_.toString)))

  override def toString: String = headers.zip(sources.sources).map { case (h, s) => s"$h - $s" }.mkString(newLineChar)
}

protected class WriteCompletionHandler(fileName: String) extends CompletionHandler[Integer, java.lang.Object] {
  private val logger: Logger = LoggerFactory.getLogger(getClass)

  override def completed(result: Integer, attachment: java.lang.Object): Unit = {
    logger.debug(s"Wrote: $result bytes to $fileName")
  }
  override def failed(exc: Throwable, attachment: java.lang.Object): Unit = {
    throw exc
  }
}
