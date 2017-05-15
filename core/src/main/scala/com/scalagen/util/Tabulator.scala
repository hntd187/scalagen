package com.scalagen.util

import de.vandermeer.asciitable.AsciiTable
import de.vandermeer.asciithemes.TA_GridThemes
import de.vandermeer.asciithemes.a7.A7_Grids
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment

/**
  * This is a util used to display data in a table, there are a lot of options which can be
  * set directly on the object. A list of options can be found at https://github.com/vdmeer/asciitable
  */
object Tabulator {

  private var _width: Int                  = 100
  private var _theme: TA_GridThemes        = TA_GridThemes.FULL
  private var _alignment: TextAlignment    = TextAlignment.CENTER
  private var _rowAlignment: TextAlignment = TextAlignment.CENTER

  private val table: AsciiTable = new AsciiTable()

  def width_=(w: Int): Unit                  = _width = w
  def theme_=(t: TA_GridThemes): Unit        = _theme = t
  def alignment_=(a: TextAlignment): Unit    = _alignment = a
  def rowAlignment_=(a: TextAlignment): Unit = _rowAlignment = a
  def width: Int                             = _width
  def theme: TA_GridThemes                   = _theme
  def alignment: TextAlignment               = _alignment
  def rowAlignment: TextAlignment            = _rowAlignment

  def print(rows: Seq[Seq[String]]): Unit = {
    print(Seq.empty[String], rows)
  }

  def print(header: Seq[String], rows: Seq[Seq[String]]): Unit = {
    table.getContext
      .setWidth(width)
      .setGridTheme(theme)
      .setGrid(A7_Grids.minusBarPlusEquals())

    if (header.nonEmpty) {
      table.addRule()
      table.addRow(header: _*).setTextAlignment(alignment)
    }

    table.addRule()
    rows.foreach(r => table.addRow(r: _*).setTextAlignment(rowAlignment))
    table.addRule()
    println(table.render())
  }
}
