package com.scalagen.writers

import java.io.File
import java.util.HashMap

import com.scalagen.data.api.{Row, _}
import com.scalagen.util.ParquetUtils

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.parquet.example.data.Group
import org.apache.parquet.example.data.simple.SimpleGroupFactory
import org.apache.parquet.hadoop.ParquetFileWriter.Mode
import org.apache.parquet.hadoop.api.WriteSupport
import org.apache.parquet.hadoop.metadata.CompressionCodecName
import org.apache.parquet.hadoop.{ParquetFileWriter, ParquetWriter}
import org.apache.parquet.schema.MessageType
import org.slf4j.{Logger, LoggerFactory}

/**
  * A writer that creates a simple Parquet file from sources. This needs to be made to be more
  * robust as currently it's very simple and probably doesn't encompass a lot of real world cases.
  * Going to also want to add partitioning to this as well.
  * Glossery of terms can be found @ the [[https://parquet.apache.org/documentation/latest/ Parquet Documentation]]
  *
  * @param sources The sources to write
  */
case class Parquet(sources: SourceContainer) extends Writer with Headers[Parquet] {

  private val logger: Logger                         = LoggerFactory.getLogger(getClass)
  private var metadata: HashMap[String, String]      = new HashMap[String, String]()
  private var pageSize: Int                          = ParquetWriter.DEFAULT_PAGE_SIZE
  private var blockSize: Int                         = ParquetWriter.DEFAULT_BLOCK_SIZE
  private var padSize: Int                           = ParquetWriter.MAX_PADDING_SIZE_DEFAULT
  private var compressionCodec: CompressionCodecName = ParquetWriter.DEFAULT_COMPRESSION_CODEC_NAME
  private var writeMode: Mode                        = ParquetFileWriter.Mode.CREATE
  private var dictionaryEncoding: Boolean            = true
  private var validationEnabled: Boolean             = true

  /**
    * What page size to use
    *
    * @param i
    * @return this object with the attribute set
    */
  def withPageSize(i: Int): Parquet = {
    pageSize = i
    this
  }

  /**
    * What block size to use
    *
    * @param i
    * @return this object with the attribute set
    */
  def withBlockSize(i: Int): Parquet = {
    blockSize = i
    this
  }

  /**
    * What padding size to use
    *
    * @param i
    * @return this object with the attribute set
    */
  def withPadSize(i: Int): Parquet = {
    padSize = i
    this
  }

  /**
    * Metadata to include in the parquet file
    *
    * @param m
    * @return this object with the attribute set
    */
  def withMetaData(m: HashMap[String, String]): Parquet = {
    metadata = m
    this
  }

  /**
    * What compression to use on the parquet file, default is uncompressed
    *
    * @param c
    * @return this object with the attribute set
    */
  def withCompressionCodec(c: CompressionCodecName): Parquet = {
    compressionCodec = c
    this
  }

  /**
    * What write mode to write the file in, default is create
    *
    * @param m
    * @return this object with the attribute set
    */
  def withWriteMode(m: Mode): Parquet = {
    writeMode = m
    this
  }
  def overwriteExisting(): Parquet = withWriteMode(ParquetFileWriter.Mode.OVERWRITE)

  /**
    * Whether or not to use dictionary encoding
    *
    * @param b
    * @return this object with the attribute set
    */
  def withDictionaryEncoding(b: Boolean): Parquet = {
    dictionaryEncoding = b
    this
  }
  def withDictionaryEncoding(): Parquet = withDictionaryEncoding(true)

  /**
    * Whether or not to validate the file as it's being written
    *
    * @param b
    * @return this object with the attribute set
    */
  def withValidation(b: Boolean): Parquet = {
    validationEnabled = b
    this
  }
  def withValidation(): Parquet = withValidation(true)

  def makeLine: Row = Row(sources.sources.map(s => s.sample()))

  def hasHeader: Boolean = true

  /**
    * The internal method to write the parquet file, it has public exposed wrappers to use
    *
    * @see [[Parquet.write()]]
    * @param path
    * @param lines
    */
  private def writeHadoop(path: Path, lines: Int) = {

    logger.debug("Created Parquet Writer with Options:")
    logger.debug(s"Page Size: $pageSize")
    logger.debug(s"Pad Size: $padSize")
    logger.debug(s"Block Size: $blockSize")
    logger.debug(s"Compression Codec: $compressionCodec")

    val schema: MessageType = ParquetUtils.makeSchema(path.getName, sources.sources, headers)
    logger.info(s"Schema is $schema")
    val parquetWriter: ParquetWriter[Group] = new ParquetWriterBuilder(path, schema, metadata)
      .withDictionaryEncoding(dictionaryEncoding)
      .withValidation(validationEnabled)
      .withMaxPaddingSize(padSize)
      .withPageSize(pageSize)
      .withRowGroupSize(blockSize)
      .withCompressionCodec(compressionCodec)
      .withWriteMode(writeMode)
      .build()

    val g: SimpleGroupFactory = new SimpleGroupFactory(schema)
    var i: Int                = 0
    while (i < lines) {
      val group: Group = g.newGroup()
      val data: Row    = makeLine
      val dLength: Int = data.length
      var x: Int       = 0
      while (x < dLength) {
        data(x) match {
          case v: Double  => group.add(headers(x), v)
          case v: Int     => group.add(headers(x), v)
          case v: Long    => group.add(headers(x), v)
          case v: Boolean => group.add(headers(x), v)
          case v: String  => group.add(headers(x), v)
          case v: Any     => group.add(headers(x), v.toString)
          case _          => throw new IllegalStateException(s"Unknown type for ${headers(x)} - ${data(x)}")
        }
        x += 1
      }
      logger.debug(s"Written: ${parquetWriter.getDataSize} total bytes")
      parquetWriter.write(group)
      i += 1
    }
    parquetWriter.close()
  }

  /**
    * The public methods used to write the Parquet file
    *
    * @param s     The file path of the file to be written
    * @param lines The number of lines to generate and write
    */
  def write(s: String, lines: Int): Unit = writeHadoop(new Path(s), lines)

  /**
    * The public methods used to write the Parquet file
    *
    * @param s     The file path to write to
    * @param lines The number of lines to generate and write
    */
  override def write(s: File, lines: Int): Unit = writeHadoop(new Path(s.getAbsolutePath), lines)

}

/**
  * A private parquet group writer as per the Parquet API
  *
  * @param path
  * @param schema
  * @param metadata
  */
private[writers] class ParquetWriterBuilder(path: Path, schema: MessageType, metadata: HashMap[String, String])
    extends ParquetWriter.Builder[Group, ParquetWriterBuilder](path) {

  override def getWriteSupport(conf: Configuration): WriteSupport[Group] = new ParquetGroupWrite(schema, metadata)

  override def self(): ParquetWriterBuilder = this
}
