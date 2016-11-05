package com.scalagen.data
import com.scalagen.data.api.SourceContainer

object MacroTests extends App {
  val c = SourceContainer(GaussianSource(), YesNoSource())
  val s = c.sources
  val cols: Any = c.r(s)
}