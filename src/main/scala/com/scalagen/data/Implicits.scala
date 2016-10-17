package com.scalagen.data

import java.math.{MathContext, RoundingMode}
import java.time.format.DateTimeFormatter

import com.scalagen.data.api.{Source, SourceContainer}

/**
  * Various default implicit values for if users do not define their own
  * they may never be used, so be lazy.
  */
object Implicits {
  implicit lazy val mc: MathContext        = new MathContext(2, RoundingMode.HALF_DOWN)
  implicit lazy val fmt: DateTimeFormatter = DateTimeFormatter.ISO_DATE

  implicit def SourceToContainer(s: Source[_, _]): SourceContainer = SourceContainer(s)
}
