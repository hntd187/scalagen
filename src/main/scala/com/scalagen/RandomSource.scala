package com.scalagen
import com.scalagen.data.api.Source

import scala.util.Random

case class RandomSource[T](classes: Seq[T]) extends Source[Random, Int] {
  val source = new Random()
  def sample(): Int = source.nextInt(classes.length)
}
