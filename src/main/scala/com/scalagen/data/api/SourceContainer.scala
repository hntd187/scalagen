package com.scalagen.data.api

/**
  * A wrapper for the composition of various sources
  * @param sources The sources
  */
case class SourceContainer(sources: Source[_, _]*) {

  /**
    * Returns a single line from the Container's current sources
    * @return A Sequence of the sampled columns
    */
  def getLine: IndexedSeq[Any] = sources.map(s => s.sample()).toIndexedSeq

  /**
    * The operator used to compose various sources together
    * @param other Another SourceContainer
    * @return A SourceContainer with the sources of both Objects
    */
  def |(other: SourceContainer): SourceContainer = SourceContainer(other.sources ++ sources: _*)

  /**
    * The operator used to compose various sources together
    * @param other A source to add to the container
    * @return A SourceContainer with the source added
    */
  def |(other: Source[_, _]): SourceContainer = SourceContainer(sources :+ other: _*)
}
