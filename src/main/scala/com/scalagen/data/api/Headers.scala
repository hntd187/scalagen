package com.scalagen.data.api

trait Headers[I] extends Sources { this: I =>
  protected var headerNames = Array.empty[String]
  def withHeaders(a: Array[String]): I = {
    require(a.length == sources.sources.length, "Must have same number of headers as columns.")
    headerNames = a
    this
  }
  def withHeaders(a: String*): I = withHeaders(a.toArray)
  def headers: IndexedSeq[String] = {
    if (headerNames.nonEmpty) {
      headerNames.toIndexedSeq
    } else {
      sources.sources.indices.map(i => s"Col$i")
    }
  }
}
