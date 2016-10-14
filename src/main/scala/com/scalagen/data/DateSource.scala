package com.scalagen.data

import java.time.format.DateTimeFormatter
import java.time.{LocalDate, Period}

import com.scalagen.data.api.Source

case class DateSource(start: LocalDate, incrementUnit: Period = Period.ofWeeks(1))(implicit fmt: DateTimeFormatter = Implicits.fmt)
    extends Source[Iterator[LocalDate], String] {

  val source: Iterator[LocalDate] = Stream.iterate[LocalDate](start)(d => d.plus(incrementUnit)).iterator
  def sample(): String            = source.next().format(fmt)
}

object DateSource {
  def apply(start: String): DateSource                        = DateSource(LocalDate.parse(start))
  def apply(start: String, incrementUnit: Period): DateSource = DateSource(LocalDate.parse(start), incrementUnit)
}
