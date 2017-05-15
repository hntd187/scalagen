package com.scalagen.data

import java.time.format.DateTimeFormatter
import java.time.{LocalDate, Period}

import com.scalagen.data.api.Source

/**
  * A Source that iterates over a time period generating a range of dates
  *
  * @param start         - the date to begin at
  * @param incrementUnit - the delta between dates
  * @param fmt           - implicit value for the date format
  */
case class DateSource(name: String = "", start: LocalDate, incrementUnit: Period = Period.ofWeeks(1))(implicit fmt: DateTimeFormatter)
    extends Source[Iterator[LocalDate], String] {

  val source: Iterator[LocalDate] = Iterator.iterate[LocalDate](start)(d => d.plus(incrementUnit))
  def sample(): String            = source.next().format(fmt)
  override def toString: String   = s"${getClass.getSimpleName}(${start.format(fmt)})"

}

object DateSource {
  def apply(start: String): DateSource = {
    DateSource(start = LocalDate.parse(start))
  }

  def apply(start: String, incrementUnit: Period): DateSource = {
    DateSource(start = LocalDate.parse(start), incrementUnit = incrementUnit)
  }
}
