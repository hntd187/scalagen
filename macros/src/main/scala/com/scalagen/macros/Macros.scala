package com.scalagen.macros

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.meta._

@compileTimeOnly("@fields not expanded")
class Fields extends StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      case q"..$mods class SourceContainer (...$paramss) extends $template" =>
          template match {

            case template"{ ..$stats } with ..$ctorcalls { $param => ..$body }" =>

              val newBody = body :+ q"""def fields = this.length """
              val newTemplate = template"{ ..$stats } with ..$ctorcalls { $param => ..$newBody }"

              q"..$mods class SourceContainer (...$paramss) extends $newTemplate"

          }
      case _ => throw new Exception("Thing happened that not work")
    }
  }
}
