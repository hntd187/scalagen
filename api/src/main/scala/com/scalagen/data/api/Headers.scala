package com.scalagen.data.api

/**
  * A trait for defining sources that have headers
  *
  * @tparam I The underlying source type for returns
  */
trait Headers[I] extends Sources { this: I =>

  protected var headerNames: Seq[String] = sources.sources.map(_.name)

  /**
    * Include header names with a writer
    *
    * @param a Headers
    * @return I
    */
  def withHeaders(a: String*): I = {
    require(a.length == sources.length, "Must have same number of headers as columns.")
    headerNames = a
    this
  }

  /**
    * Generates the header names for a writer. If none are set it will generate automatic header
    * names, otherwise return the header names.
    *
    * @return Sequence of header names
    */
  def headers: IndexedSeq[String] = {
    headerNames.zipWithIndex.map {
      case (name, i) if name.isEmpty => s"Col$i"
      case (name, _)                 => name
    }.toIndexedSeq
  }
}
