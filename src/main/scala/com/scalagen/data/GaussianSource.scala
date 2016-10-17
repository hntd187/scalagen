package com.scalagen.data

import java.math.MathContext

import breeze.stats.distributions.Gaussian
import com.scalagen.data.api.Source

/**
  * This source draws random numbers according to a Gaussian Distribution
  * @param mu - Mu statistic of the distribution
  * @param sigma - Sigma statistic of the distribution
  * @param mc - implicit MathContext for how to format the double
  */
case class GaussianSource(mu: Double = 2.0, sigma: Double = 1.0)(implicit val mc: MathContext = Implicits.mc) extends Source[Gaussian, Double] {
  override val source   = Gaussian(mu, sigma)
  override def sample() = BigDecimal(source.sample(), mc).doubleValue()
}
