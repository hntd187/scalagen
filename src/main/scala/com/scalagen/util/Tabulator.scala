package com.scalagen.util

import de.vandermeer.asciitable.v2.V2_AsciiTable
import de.vandermeer.asciitable.v2.render.{V2_AsciiTableRenderer, V2_Width, WidthAbsoluteEven}
import de.vandermeer.asciitable.v2.themes.{V2_E_TableThemes, V2_TableTheme}

/**
  * This is a util used to display data in a table, there are a lot of options which can be
  * set directly on the object. A list of options can be found at https://github.com/vdmeer/asciitable
  */
object Tabulator {

  private var _width: V2_Width           = new WidthAbsoluteEven(100)
  private var _theme: V2_TableTheme      = V2_E_TableThemes.UTF_LIGHT_DOUBLE.get()
  private var _alignment: Array[Char]    = Array.emptyCharArray
  private var _rowAlignment: Array[Char] = Array.emptyCharArray

  private val table: V2_AsciiTable            = new V2_AsciiTable()
  private val renderer: V2_AsciiTableRenderer = new V2_AsciiTableRenderer()

  def width_=(w: V2_Width): Unit           = _width = w
  def theme_=(t: V2_TableTheme): Unit      = _theme = t
  def alignment_=(a: Array[Char]): Unit    = _alignment = a
  def rowAlignment_=(a: Array[Char]): Unit = _rowAlignment = a
  def width: V2_Width                      = _width
  def theme: V2_TableTheme                 = _theme
  def alignment: Array[Char]               = _alignment
  def rowAlignment: Array[Char]            = _rowAlignment

  def print(rows: Seq[Seq[String]]): Unit = {
    print(Seq.empty[String], rows)
  }

  def print(header: Seq[String], rows: Seq[Seq[String]]): Unit = {
    renderer.setTheme(theme).setWidth(width)
    if (rowAlignment.isEmpty) rowAlignment = Array.fill(header.length)('c')
    if (header.nonEmpty) {
      if (alignment.isEmpty) alignment = Array.fill(header.length)('c')
      table.addRule()
      table.addRow(header: _*).setAlignment(alignment)

    }
    table.addRule()
    rows.foreach(r => table.addRow(r: _*).setAlignment(rowAlignment))
    table.addRule()
    println(renderer.render(table))
    table.getTable.clear()
  }
}
