package com.scalagen.data
import com.scalagen.data.api.Source

/**
  * A source that counts upward from a start value
  * @param start - where to start counting
  * @see [[DeincrementingSource]]
  */
case class IncrementingSource(start: Int = 1) extends Source[Iterator[Int], Int] {
  private[data] val source: Iterator[Int] = Stream.from(start).iterator
  def sample(): Int                       = source.next
}

/**
  * Same as an [[IncrementingSource]] except goes down instead of up
  * @param start where to count down from
  * @see [[DeincrementingSource]]
  */
case class DeincrementingSource(start: Int = 1) extends Source[Iterator[Int], Int] {
  private[data] val source: Iterator[Int] = Stream.from(start).reverseIterator
  def sample(): Int                       = source.next
}
