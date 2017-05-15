package com.scalagen.data

import java.nio.file._
import java.time.LocalDate
import scala.io._

import com.scalagen.data.api.SourceContainer
import com.scalagen.util.ParquetUtils
import com.scalagen.writers.{Csv, Parquet}

import org.scalatest._
import org.slf4j.{Logger, LoggerFactory}

case object LinuxOnly extends Tag("com.scalagen.windows")

class WriterTestSuite extends TestSpec {

  val logger: Logger = LoggerFactory.getLogger(getClass)

  val gs: GaussianSource      = GaussianSource(mu = 5.0, sigma = 20)
  val emptyGs: GaussianSource = GaussianSource()
  val headers: Array[String]  = Array("Header1", "Header2")
  val path: Path              = Paths.get("test.csv")
  val parquetPath: Path       = Paths.get("test.parquet")
  val parquetCrc: Path        = Paths.get(".test.parquet.crc")

  val c: Csv = Csv {
    gs | emptyGs
  }

  val p: Parquet = Parquet {
    gs | IncrementingSource() | GenderSource() | GaussianSource() | DateSource(start = LocalDate.now()) | BernoulliSource()
  }

  def cleanUp(): Unit = {
    try {
      Files.deleteIfExists(path)
      Files.deleteIfExists(parquetPath)
      Files.deleteIfExists(parquetCrc)
    } catch {
      case _: Exception => logger.warn("Couldn't clean up existing files, this might not work...")
    }
  }

  override def beforeAll(): Unit = {
    cleanUp()
  }

  override def afterAll: Unit = {
    cleanUp()
  }

  describe("Csv Writer") {
    describe("Csv Headers") {
      it("Should not be empty") {
        c.headers should not be empty
      }
      it("Should be the correct length") {
        c.headers should have length 2
      }
      it("Should create placeholder column headers") {
        c.headers should contain allOf ("Col0", "Col1")
      }

      it("Should return correct headers when set") {
        c.withHeaders("Header1", "Header2")
        c.headers should be(headers)
      }
    }

    describe("Csv Sources") {
      it("Should not be empty") {
        c.sources.sources should not be empty
      }
      it("Should be the correct length") {
        c.sources.sources.length should be(2)
      }
      it("Should contain the correct sources") {
        c.sources.sources.head shouldBe theSameInstanceAs(gs)
        c.sources.sources.last shouldBe theSameInstanceAs(emptyGs)
      }
    }

    describe("Csv Line Generators") {
      it("Should generate a valid line") {
        c.makeCsvLine should fullyMatch regex s"""^"-?[\\d\\.]+"\\|"-?[\\d\\.]+"${System.lineSeparator}$$"""
      }
      it("Should generate valid line without quotes") {
        c.withQuoted(false)
        c.makeCsvLine should fullyMatch regex s"""^-?[\\d\\.]+\\|-?[\\d\\.]+${System.lineSeparator}$$"""
      }
    }
    describe("Writing Csv") {
      it("Should write csv file") {
        c.write("test.csv", 10)
        val size = Source.fromFile(path.toString).getLines().size
        size shouldBe 11
        Files.exists(path) shouldBe true
      }
    }
  }

  ignore("Parquet Writer") {
    it("Should write Parquet file", LinuxOnly) {
      p.write("test.parquet", 10)
      Files.exists(parquetPath) shouldBe true
      ParquetUtils.parquetRowCount(parquetPath.toString) shouldBe 10L
    }
  }

  describe("Source Container Tests") {
    it("2 sources should be a SourceContainer") {
      (GaussianSource() | GaussianSource()) shouldBe a[SourceContainer]
    }
    it("A SourceContainer and a Source should be a SourceContainer") {
      (SourceContainer(GaussianSource()) | GaussianSource()) shouldBe a[SourceContainer]
    }
    it("2 SourceContainers should be a SourceContainer") {
      (SourceContainer(GaussianSource()) | SourceContainer(GaussianSource())) shouldBe a[SourceContainer]
    }
    it("SourceContainer with more than 1 source should append") {
      val sc = SourceContainer(GaussianSource(), GaussianSource()) | GaussianSource()
      sc shouldBe a[SourceContainer]
      sc.sources should have length 3
      sc.sources.foreach(_ shouldBe a[GaussianSource])
    }
  }

  describe("Tabulator Tests") {
    it("Should print a nice ascii table") {
      c.show()
    }
  }
}
