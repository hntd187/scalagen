package com.scalagen.data

import breeze.stats.distributions.Bernoulli
import com.scalagen.data.api.Source

/**
  * Source based on Bernoulli Trials
  * @param p - probability of success in the trial
  */
case class BernoulliSource(p: Double = 0.5) extends Source[Bernoulli, Boolean] {
  val source: Bernoulli = Bernoulli.distribution(p)
  def sample(): Boolean = source.draw()
}

/**
  * Yes or No based on Bernoulli Trial
  * @param p - probability of yes in trial
  */
case class YesNoSource(p: Double = 0.5) extends Source[Bernoulli, String] {
  val source: Bernoulli = Bernoulli.distribution(p)
  def sample(): String = source.draw() match {
    case true  => "Yes"
    case false => "No"
  }
}

/**
  * M or F based on Bernoulli Trial
  * @param p - probability of F in trial
  */
case class GenderSource(p: Double = 0.5) extends Source[Bernoulli, String] {
  val source: Bernoulli = Bernoulli.distribution(p)
  def sample(): String = source.draw() match {
    case true  => "F"
    case false => "M"
  }
}
