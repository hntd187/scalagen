package com.scalagen.data.api
import scala.reflect.ClassTag

abstract class Source[S, V](implicit s: ClassTag[S], v: ClassTag[V]) {
  private[data] val source: S
  def sample(): V
  def |(other: Source[_, _]): SourceContainer = SourceContainer(this, other)
  override def toString: String               = source.toString
}
