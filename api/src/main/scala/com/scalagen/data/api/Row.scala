package com.scalagen.data.api

import java.time.LocalDate

case class Row(protected[scalagen] val values: Seq[Any]) {
  def apply(i: Int): Any             = values(i)
  def get[@specialized T](i: Int): T = values(i).asInstanceOf[T]
  def getInt(i: Int): Int            = get[Int](i)
  def getFloat(i: Int): Float        = get[Float](i)
  def getDouble(i: Int): Double      = get[Double](i)
  def getLong(i: Int): Long          = get[Long](i)
  def getShort(i: Int): Short        = get[Short](i)
  def getBoolean(i: Int): Boolean    = get[Boolean](i)
  def getChar(i: Int): Char          = get[Char](i)
  def getByte(i: Int): Byte          = get[Byte](i)
  def getString(i: Int): String      = apply(i).toString
  def getDate(i: Int): LocalDate     = get[LocalDate](i)

  def map[@specialized T](f: Any => T): Row = Row(values.map(f))
  def mkString(sep: String): String         = values.mkString(sep)
  def zip[T](other: Seq[T]): Seq[(Any, T)]  = values.zip(other)
  def length: Int                           = values.length

  override def toString: String = values.mkString(", ")
}

object Row {
  def apply(size: Int): Row = new Row(new Array[Any](size))
}
