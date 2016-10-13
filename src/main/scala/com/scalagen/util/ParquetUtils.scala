package com.scalagen.util

import com.scalagen.data.api.Source
import com.scalagen.data.{BernoulliSource, DateSource, GaussianSource, IncrementingSource}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileStatus, FileSystem, Path}
import org.apache.parquet.hadoop.ParquetFileReader
import org.apache.parquet.schema.OriginalType._
import org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName._
import org.apache.parquet.schema.Type.Repetition._
import org.apache.parquet.schema.{MessageType, Type, Types}

import scala.collection.JavaConverters._

object ParquetUtils {

  def makeSchema(s: String, sources: Seq[Source[_, _]], headers: Seq[String]): MessageType = {
    val sourceTypes: Seq[Type] = sources.zip(headers).map {
      case (s: Source[_, _], n: String) => sourceToParquetType(s, n)
    }
    new MessageType(s, sourceTypes: _*)
  }

  def sourceToParquetType(s: Source[_, _], columnName: String): Type = {
    s match {
      case _: GaussianSource     => Types.primitive(DOUBLE, REQUIRED).named(columnName)
      case _: IncrementingSource => Types.primitive(INT32, REQUIRED).named(columnName)
      case _: DateSource         => Types.primitive(BINARY, REQUIRED).as(UTF8).named(columnName)
      case _: BernoulliSource    => Types.primitive(BOOLEAN, REQUIRED).as(UTF8).named(columnName)
      case _                     => Types.primitive(BINARY, REQUIRED).as(UTF8).named(columnName)
    }
  }

  def parquetRowCount(s: String): Long = {
    parquetRowCount(new Path(s))
  }

  def parquetRowCount(p: Path, conf: Configuration = new Configuration()): Long = {
    val fs: FileSystem     = p.getFileSystem(conf)
    val status: FileStatus = fs.getFileStatus(p)
    ParquetFileReader.readFooters(conf, status, false).asScala.head.getParquetMetadata.getBlocks.asScala.map(_.getRowCount).sum
  }
}
