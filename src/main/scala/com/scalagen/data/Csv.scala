package com.scalagen.data

import java.nio.ByteBuffer
import java.nio.channels.{AsynchronousFileChannel, FileLock}
import java.nio.file.StandardOpenOption._
import java.nio.file.{Path, Paths}
import java.util.concurrent.atomic.{AtomicInteger, AtomicLong}

import com.scalagen.data.api._
import com.scalagen.util.Tabulator
import org.slf4j.{Logger, LoggerFactory}

case class Csv(sources: SourceContainer) extends Writer with Headers[Csv] {

  private val logger: Logger  = LoggerFactory.getLogger(getClass)
  private var delim: String   = "|"
  private var quote: String   = "\""
  private var quoted: Boolean = true
  private var newLine: String = System.lineSeparator()

  def withDelim(d: String): Csv = {
    delim = d
    this
  }

  def withQuote(q: String): Csv = {
    quote = q
    this
  }

  def withQuoted(q: Boolean): Csv = {
    quoted = q
    this
  }

  def withNewLine(n: String): Csv = {
    newLine = n
    this
  }

  def makeLine: Seq[Any] = sources.getLine

  private[data] def makeCsvLine: String = {
    val line = StringBuilder.newBuilder
    if (quoted) {
      makeLine.map(f => f"""\"$f%s\"""").foreach(f => line.append(f).append(delim))
    } else {
      makeLine.foreach(f => line.append(f).append(delim))
    }
    line.update(line.length - 1, '\n')
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
      val head                  = headers.mkString(delim) + '\n'
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
    val lastLine: String   = makeCsvLine.stripSuffix("\n")
    val buffer: ByteBuffer = ByteBuffer.wrap(lastLine.getBytes)
    channel.write(buffer, position.get)
    channel.close()
  }

  def show(n: Int = 10): Unit = Tabulator.print(headers, Seq.fill(n)(makeLine.map(_.toString)))

  override def toString: String = headers.zip(sources.sources).map { case (h, s) => s"$h - $s" }.mkString("\n")

}
