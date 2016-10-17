package com.scalagen.data.api
import scala.reflect.ClassTag

/**
  *
  * @param s S implicit ClassTag
  * @param v V implicit ClassTag
  * @tparam S The type of the source being sampled from
  * @tparam V The type of the value returned from the source
  */
abstract class Source[S, V](implicit s: ClassTag[S], v: ClassTag[V]) {

  private[data] val source: S

  /**
    * Samples the source for a single value
    * @return a single value from the source
    */
  def sample(): V

  /**
    * The operator used to compose columns of a data source
    * @param other The Source being combined with
    * @return A source container containing both Sources
    */
  def |(other: Source[_, _]): SourceContainer = SourceContainer(this, other)

  override def toString: String = source.toString
}
