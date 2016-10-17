package com.scalagen.data.api

/**
  * A trait for defining sources that have headers
  * @tparam I The underlying source type for returns
  */
trait Headers[I] extends Sources { this: I =>

  protected var headerNames = Array.empty[String]

  /**
    * Include header names with a writer
    * @param a Headers
    * @return I
    */
  def withHeaders(a: Array[String]): I = {
    require(a.length == sources.sources.length, "Must have same number of headers as columns.")
    headerNames = a
    this
  }

  def withHeaders(a: String*): I = withHeaders(a.toArray)

  /**
    * Generates the header names for a writer. If none are set it will generate automatic header
    * names, otherwise return the header names.
    * @return Sequence of header names
    */
  def headers: IndexedSeq[String] = {
    if (headerNames.nonEmpty) {
      headerNames.toIndexedSeq
    } else {
      sources.sources.indices.map(i => s"Col$i")
    }
  }
}
