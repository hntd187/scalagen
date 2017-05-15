package com.scalagen

import java.math.{MathContext, RoundingMode}
import java.time.format.DateTimeFormatter

import com.scalagen.data.api.{Source, SourceContainer}

/**
  * Created by hntd on 5/9/2017.
  */
package object data {
  implicit lazy val mc: MathContext        = new MathContext(2, RoundingMode.HALF_DOWN)
  implicit lazy val fmt: DateTimeFormatter = DateTimeFormatter.ISO_DATE

  implicit def SourceToContainer(s: Source[_, _]): SourceContainer = SourceContainer(s)
}
