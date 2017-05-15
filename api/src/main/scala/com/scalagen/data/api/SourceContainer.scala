package com.scalagen.data.api

import com.scalagen.macros.Fields

/**
  * A wrapper for the composition of various sources
  *
  * @param s2 The sources
  */
@Fields
case class SourceContainer(private val s1: Source[_, _], private val s2: Source[_, _]*) {
  def sources: Seq[Source[_, _]] = s2 :+ s1

  /**
    * Returns a single line from the Container's current sources
    *
    * @return A Sequence of the sampled columns
    */
  def getLine: Row = Row(sources.map(s => s.sample()))

  def length: Int = sources.length

  /**
    * The operator used to compose various sources together
    *
    * @param other Another SourceContainer
    * @return A SourceContainer with the sources of both Objects
    */
  def |(other: SourceContainer): SourceContainer = SourceContainer(s1, s2 ++ other.sources: _*)

  /**
    * The operator used to compose various sources together
    *
    * @param other A source to add to the container
    * @return A SourceContainer with the source added
    */
  def |(other: Source[_, _]): SourceContainer = SourceContainer(other, sources: _*)

}
