package com.scalagen.data

import java.util.HashMap

import org.apache.hadoop.conf.Configuration
import org.apache.parquet.example.data.{Group, GroupWriter}
import org.apache.parquet.hadoop.api.WriteSupport
import org.apache.parquet.hadoop.api.WriteSupport.WriteContext
import org.apache.parquet.io.api.RecordConsumer
import org.apache.parquet.schema.MessageType

class ParquetGroupWrite(schema: MessageType, metadata: HashMap[String, String]) extends WriteSupport[Group] {
  private var writer = None: Option[GroupWriter]

  override def init(configuration: Configuration): WriteContext = {
    new WriteContext(schema, metadata)
  }

  override def write(t: Group): Unit = writer.get.write(t)
  override def prepareForWrite(recordConsumer: RecordConsumer): Unit = {
    writer = Some(new GroupWriter(recordConsumer, schema))
  }
}
