package com.scalagen.data

import java.nio.ByteBuffer
import java.nio.channels.{AsynchronousFileChannel, FileLock}
import java.nio.file.StandardOpenOption._
import java.nio.file.{Path, Paths}
import java.util.concurrent.atomic.{AtomicInteger, AtomicLong}

import com.scalagen.data.api._
import com.scalagen.util.Tabulator
import org.slf4j.{Logger, LoggerFactory}

/**
  * A writer that writes a CSV file based on options provided
  * @param sources - Sources for writing lines to the file
  */
case class Csv(sources: SourceContainer) extends Writer with Headers[Csv] {

  private val logger: Logger  = LoggerFactory.getLogger(getClass)
  private var delim: String   = "|"
  private var quote: String   = "\""
  private var quoted: Boolean = true
  private var newLine: String = System.lineSeparator()

  /**
    * What delimiter to use for lines, default is |
    * @param d new delimiters to use
    * @return object with attribute set
    */
  def withDelim(d: String): Csv = {
    delim = d
    this
  }

  /**
    * What character to use for quoting values, default is "
    * @param q new quote character to use
    * @return object with attribute set
    */
  def withQuote(q: String): Csv = {
    quote = q
    this
  }

  /**
    * Whether or not to quote values when writing them, default is true
    * @param q new value to use
    * @return object with attribute set
    */
  def withQuoted(q: Boolean): Csv = {
    quoted = q
    this
  }

  /**
    * What new line character to use for separating lines, default is system new line
    * @param n new line value to use
    * @return object with attribute set
    */
  def withNewLine(n: String): Csv = {
    newLine = n
    this
  }

  def makeLine: Seq[Any] = sources.getLine

  private[data] def makeCsvLine: String = {
    val line = StringBuilder.newBuilder
    if (quoted) {
      val l = makeLine.map(f => s"""$quote$f$quote""").mkString(delim)
      line.append(l)
    } else {
      val l = makeLine.mkString(delim)
      line.append(l)
    }
    line.append(newLine)
    line.result()
  }

  def hasHeader = true

  def write(s: String, lines: Int): Unit = {
    // Default is to write a separated file with a header.
    val path: Path                       = Paths.get(s)
    val channel: AsynchronousFileChannel = AsynchronousFileChannel.open(path, TRUNCATE_EXISTING, WRITE, CREATE)
    val position: AtomicLong             = new AtomicLong(0L)
    val i: AtomicInteger                 = new AtomicInteger(1)

    if (hasHeader) {
      val head: String          = headers.mkString(delim) + newLine
      val headBytes: ByteBuffer = ByteBuffer.wrap(head.getBytes)
      channel.write(headBytes, position.getAndAdd(headBytes.capacity.toLong))
      i.incrementAndGet()
    }
    while (i.get() <= lines) {
      val line: String       = makeCsvLine
      val buffer: ByteBuffer = ByteBuffer.wrap(line.getBytes)
      val lock: FileLock     = channel.lock().get()
      if (lock.isValid) {
        val writeOp: Integer = channel.write(buffer, position.getAndAdd(buffer.capacity.toLong)).get()
        logger.debug(s"Wrote: $writeOp bytes to $s")
        i.getAndIncrement()
        lock.release()
      }
    }
    val lastLine: String   = makeCsvLine.stripSuffix(newLine)
    val buffer: ByteBuffer = ByteBuffer.wrap(lastLine.getBytes)
    channel.write(buffer, position.get)
    channel.close()
  }

  def show(n: Int = 10): Unit = Tabulator.print(headers, Seq.fill(n)(makeLine.map(_.toString)))

  override def toString: String = headers.zip(sources.sources).map { case (h, s) => s"$h - $s" }.mkString(newLine)

}
