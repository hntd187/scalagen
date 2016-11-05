package com.scalagen.data

import java.math.MathContext

import com.scalagen.data.api.Source

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._
import scala.util.Random

/**
  * A Source that draws randomly from predetermined classes
  * @param classes The random classes to select from.
  * @tparam T The Sequence type for sampling
  */
case class RandomSource[T: TypeTag](classes: Seq[T])(implicit t: ClassTag[T]) extends Source[Random, T] {
  private[data] val source: Random = new Random()
  def sample(): T                  = classes(source.nextInt(classes.length))
}

/**
  * A Source that randomly draws Double values within the range
  * @param low - low end of the range inclusive
  * @param high - high end of the range inclusive
  * @param step - the step between values
  * @param mc - implicit MathContext for how to format the Double
  */
case class RandomDouble(low: Double, high: Double, step: Double)(implicit mc: MathContext = Implicits.mc) extends Source[Random, Double] {
  private[data] val source = new Random()
  def sample(): Double     = BigDecimal(source.nextDouble(), mc).doubleValue()
}

/**
  * A Source that randomly draws Integer values within the range
  * @param low - low end of the range inclusive
  * @param high - high end of the range inclusive
  * @param step - the step between values
  */
case class RandomInt(low: Int, high: Int, step: Int) extends Source[Random, Int] {
  private[data] val source: Random = new Random()
  def sample(): Int                = source.nextInt(high)
}
