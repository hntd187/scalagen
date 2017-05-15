package com.scalagen
package writers

import com.scalagen.data.IncrementingSource
import com.scalagen.data.api._

import org.slf4j.{Logger, LoggerFactory}

/**
  * A writer that doesn't actually write anything to disk, but provides an in memory collection of rows.
  */
case class InMemory(sources: SourceContainer) extends Headers[InMemory] {
  private val logger: Logger           = LoggerFactory.getLogger(getClass)
  private var currentRowData: Seq[Row] = Seq.empty[Row]
  private var currentLength: Int       = 0

  /**
    * Whether or not the writer source writes headers
    *
    * @return Do I write headers?
    */
  def hasHeader: Boolean = true

  def makeLines(num: Int = 10): Seq[Row] = {
    if (currentLength == num && currentRowData.nonEmpty) {
      logger.info(s"Already have a $num record long collection in memory, returning that instead...")
      currentRowData
    } else {
      currentLength = num
      currentRowData = Seq.fill(num)(sources.getLine)
      currentRowData
    }
  }

}

object Main extends App {
  val container = IncrementingSource() | IncrementingSource() | IncrementingSource()
  println(container.fields)
}
