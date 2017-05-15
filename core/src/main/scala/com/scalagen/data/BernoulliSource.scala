package com.scalagen.data

import com.scalagen.data.api.Source

import breeze.stats.distributions.Bernoulli

/**
  * Source based on Bernoulli Trials
  *
  * @param p - probability of success in the trial
  */
case class BernoulliSource(name: String = "", p: Double = 0.5) extends Source[Bernoulli, Boolean] {
  val source: Bernoulli = Bernoulli.distribution(p)
  def sample(): Boolean = source.draw()
}

/**
  * Yes or No based on Bernoulli Trial
  *
  * @param p - probability of yes in trial
  */
case class YesNoSource(name: String = "", p: Double = 0.5) extends Source[Bernoulli, String] {
  val source: Bernoulli = Bernoulli.distribution(p)
  def sample(): String =
    if (source.draw()) "Yes"
    else "No"
}

/**
  * M or F based on Bernoulli Trial
  *
  * @param p - probability of F in trial
  */
case class GenderSource(name: String = "", p: Double = 0.5) extends Source[Bernoulli, String] {
  val source: Bernoulli = Bernoulli.distribution(p)
  def sample(): String =
    if (source.draw()) "F"
    else "M"

}
