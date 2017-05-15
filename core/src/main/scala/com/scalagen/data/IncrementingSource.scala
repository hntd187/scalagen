package com.scalagen.data

import com.scalagen.data.api.Source

/**
  * A source that counts upward from a start value
  *
  * @param start - where to start counting
  * @see [[DeincrementingSource]]
  */
case class IncrementingSource(name: String = "", start: Int = 0) extends Source[Iterator[Int], Int] {
  val source: Iterator[Int]     = Iterator.from(start)
  def sample(): Int             = source.next
  override def toString: String = s"${getClass.getSimpleName}($start)"
}

/**
  * Same as an [[IncrementingSource]] except goes down instead of up
  *
  * @param start where to count down from
  * @see [[DeincrementingSource]]
  */
case class DeincrementingSource(name: String = "", start: Int = 0) extends Source[Iterator[Int], Int] {
  val source: Iterator[Int]     = Iterator.from(start, -1)
  def sample(): Int             = source.next
  override def toString: String = s"${getClass.getSimpleName}($start)"
}
