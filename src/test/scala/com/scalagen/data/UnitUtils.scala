package com.scalagen.data

import java.io.ByteArrayOutputStream

object UnitUtils {

  def captureOutput[B](o: => B): ByteArrayOutputStream = {
    val output = new ByteArrayOutputStream()
    Console.withOut(output) {
      o
    }
    output
  }

}
