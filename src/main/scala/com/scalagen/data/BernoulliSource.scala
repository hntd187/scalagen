package com.scalagen.data

import breeze.stats.distributions.Bernoulli
import com.scalagen.data.api.Source

case class BernoulliSource(p: Double = 0.5) extends Source[Bernoulli, Boolean] {
  val source: Bernoulli = Bernoulli.distribution(p)
  def sample(): Boolean = source.draw()
}

case class YesNoSource(p: Double = 0.5) extends Source[Bernoulli, String] {
  val source: Bernoulli = Bernoulli.distribution(p)
  def sample(): String = source.draw() match {
    case true  => "Yes"
    case false => "No"
  }
}

case class GenderSource(p: Double = 0.5) extends Source[Bernoulli, String] {
  val source: Bernoulli = Bernoulli.distribution(p)
  def sample(): String = source.draw() match {
    case true  => "F"
    case false => "M"
  }
}
