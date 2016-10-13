package com.scalagen.data
import com.scalagen.data.api.Source

case class IncrementingSource(start: Int = 1) extends Source[Iterator[Int], Int] {
  override val source: Iterator[Int] = Stream.from(start).iterator
  override def sample(): Int         = source.next
}
