package com.scalagen.data.api

/**
  * Base trait for the source API to derive from
  */
trait Sources {
  val sources: SourceContainer
  def length: Int = sources.length
}
