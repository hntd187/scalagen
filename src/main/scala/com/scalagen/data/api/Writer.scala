package com.scalagen.data.api

import java.io.File

import com.scalagen.writers.Row

/**
  * A trait that allows writers to actually write things.
  */
trait Writer {

  /**
    * Generates a single line for the writer to take
    * @return A sequence of column values to write
    */
  def makeLine: Row

  /**
    * Whether or not the writer source writes headers
    * @return Do I write headers?
    */
  def hasHeader: Boolean

  /**
    * The actual method to write lines to a file
    * @param s The file path of the file to be written
    * @param lines The number of lines to generate and write
    */
  def write(s: String, lines: Int): Unit

  /**
    * The actual method to write lines to a file
    * @param s The file path to write to
    * @param lines The number of lines to generate and write
    */
  def write(s: File, lines: Int): Unit = write(s.getAbsolutePath, lines)

}
