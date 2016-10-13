package com.scalagen.data.api

case class SourceContainer(sources: Source[_, _]*) {
  def getLine: Seq[String]                       = sources.map(s => s.sample().toString)
  def |(other: SourceContainer): SourceContainer = SourceContainer(other.sources ++ sources: _*)
  def |(other: Source[_, _]): SourceContainer    = SourceContainer(sources :+ other: _*)
}