package com.scalagen.data

import java.math.MathContext

import com.scalagen.data.api.Source

import breeze.stats.distributions.Gaussian

/**
  * This source draws random numbers according to a Gaussian Distribution
  *
  * @param mu    - Mu statistic of the distribution
  * @param sigma - Sigma statistic of the distribution
  * @param mc    - implicit MathContext for how to format the double
  */
case class GaussianSource(name: String = "", mu: Double = 2.0, sigma: Double = 1.0)(implicit val mc: MathContext)
    extends Source[Gaussian, Double] {
  override val source: Gaussian = Gaussian(mu, sigma)
  override def sample(): Double = BigDecimal(source.sample(), mc).doubleValue()
}
