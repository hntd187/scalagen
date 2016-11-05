package com.scalagen
package writers

import com.scalagen.data.api.{Headers, SourceContainer}
import org.slf4j.{Logger, LoggerFactory}

/**
  * A writer that doesn't actually write anything to disk, but provides an in memory collection of rows.
  */
case class InMemory(sources: SourceContainer) extends Headers[InMemory] {
  private val logger: Logger = LoggerFactory.getLogger(getClass)
  private var data           = Seq.empty[Row]

  /**
    * Whether or not the writer source writes headers
    *
    * @return Do I write headers?
    */
  def hasHeader: Boolean = true

  def makeLines(num: Int = 10): Seq[Row] = {
    data = Seq.fill(num)(sources.getLine)
    data
  }

}
