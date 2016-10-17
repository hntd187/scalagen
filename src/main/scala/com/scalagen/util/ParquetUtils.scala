package com.scalagen.util

import com.scalagen.data._
import com.scalagen.data.api.Source
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileStatus, FileSystem, Path}
import org.apache.parquet.hadoop.ParquetFileReader
import org.apache.parquet.schema.OriginalType._
import org.apache.parquet.schema.PrimitiveType.PrimitiveTypeName._
import org.apache.parquet.schema.{MessageType, Type, Types}

import scala.collection.JavaConverters._

/**
  * Utils to assist in creating a parquet schema according to what source is provided
  */
object ParquetUtils {

  private[scalagen] def makeSchema(s: String, sources: Seq[Source[_, _]], headers: Seq[String]): MessageType = {
    val sourceTypes: Seq[Type] = sources.zip(headers).map {
      case (s: Source[_, _], n: String) => sourceToParquetType(s, n)
    }
    new MessageType(s, sourceTypes: _*)
  }

  private[scalagen] def sourceToParquetType(s: Source[_, _], columnName: String): Type = {
    s match {
      case _: GaussianSource     | _: RandomDouble                        => Types.required(DOUBLE).named(columnName)
      case _: IncrementingSource | _: DeincrementingSource | _: RandomInt => Types.required(INT32).named(columnName)
      case _: DateSource                                                  => Types.required(BINARY).as(UTF8).named(columnName)
      case _: BernoulliSource                                             => Types.required(BOOLEAN).named(columnName)
      case _                                                              => Types.required(BINARY).as(UTF8).named(columnName)
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
