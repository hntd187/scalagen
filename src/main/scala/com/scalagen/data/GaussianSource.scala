package com.scalagen.data

import java.math.MathContext

import breeze.stats.distributions.Gaussian
import com.scalagen.data.api.Source

case class GaussianSource(mu: Double = 2.0, sigma: Double = 1.0)(implicit val mc: MathContext = Implicits.mc) extends Source[Gaussian, Double] {
  override val source   = Gaussian(mu, sigma)
  override def sample() = BigDecimal(source.sample(), mc).doubleValue()
}
