package com.scalagen.data

import java.math.MathContext

import com.scalagen.data.api.Source

import scala.collection.immutable.NumericRange
import scala.reflect.ClassTag
import scala.util.Random

/**
  * A Source that draws randomly from predetermined classes
  * @param classes The random classes to select from.
  * @tparam T The Sequence type for sampling
  */
case class RandomSource[T](classes: Seq[T])(implicit t: ClassTag[T]) extends Source[Random, T] {
  val source: Random = new Random()
  def sample(): T    = classes(source.nextInt(classes.length))
}

private[data] abstract class RandomNumber[@specialized T: ClassTag] extends Source[NumericRange[T], T] {
  private[data] val source: NumericRange[T]
  def sample(): T = source(Random.nextInt(source.length))
}

/**
  * A Source that randomly draws Double values within the range
  * @param low - low end of the range inclusive
  * @param high - high end of the range inclusive
  * @param step - the step between values
  * @param mc - implicit MathContext for how to format the Double
  */
case class RandomDouble(low: Double, high: Double, step: Double)(implicit mc: MathContext = Implicits.mc) extends RandomNumber[Double] {
  private[data] val source: NumericRange[Double] = Range.Double.inclusive(low, high, step)
  override def sample(): Double = BigDecimal(super.sample(), mc).doubleValue()
}

/**
  * A Source that randomly draws Integer values within the range
  * @param low - low end of the range inclusive
  * @param high - high end of the range inclusive
  * @param step - the step between values
  */
case class RandomInt(low: Int, high: Int, step: Int) extends RandomNumber[Int] {
  private[data] val source: NumericRange[Int] = Range.Int.inclusive(low, high, step)
}
