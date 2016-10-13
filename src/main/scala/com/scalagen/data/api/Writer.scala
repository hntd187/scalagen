package com.scalagen.data.api

import java.io.File

trait Writer {
  def makeLine: Seq[Any]
  def hasHeader: Boolean
  def write(s: String, lines: Int): Unit
  def write(s: File, lines: Int): Unit = write(s.getAbsolutePath, lines)
}
