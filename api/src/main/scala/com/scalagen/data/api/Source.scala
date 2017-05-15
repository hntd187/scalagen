package com.scalagen.data.api

import scala.reflect.runtime.universe._

/**
  *
  * @param s S implicit ClassTag
  * @param v V implicit ClassTag
  * @tparam S The type of the source being sampled from
  * @tparam V The type of the value returned from the source
  */
abstract class Source[S, V](implicit val s: TypeTag[S], val v: TypeTag[V]) {
  private[data] def S: Type = s.tpe
  private[data] def V: Type = v.tpe

  private[data] val source: S

  val name: String

  /**
    * Samples the source for a single value
    *
    * @return a single value from the source
    */
  def sample(): V

  /**
    * The operator used to compose columns of a data source
    *
    * @param other The Source being combined with
    * @return A source container containing both Sources
    */
  def |(other: Source[_, _]): SourceContainer = SourceContainer(other, this)

  override def toString: String = source.toString
}
