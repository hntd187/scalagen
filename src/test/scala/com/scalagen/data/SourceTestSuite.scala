package com.scalagen.data

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import breeze.stats.distributions.{Bernoulli, Gaussian}
import com.scalagen.data.api.Source
import org.scalatest._
import org.slf4j.{Logger, LoggerFactory}

import scala.reflect.runtime.universe._
import scala.util.Random

class SourceTestSuite extends FunSpec with Matchers {
  val logger: Logger = LoggerFactory.getLogger(getClass)

  val gs: GaussianSource        = GaussianSource(1.0, 2.0)
  val bs: BernoulliSource       = BernoulliSource()
  val ds: DateSource            = DateSource(LocalDate.now())
  val yns: YesNoSource          = YesNoSource()
  val ges: GenderSource         = GenderSource()
  val des: DeincrementingSource = DeincrementingSource()
  val ri: RandomInt             = RandomInt(0, 10, 1)
  val rd: RandomDouble          = RandomDouble(0.0, 10.0, 1.0)
  val rc: RandomSource[String]  = RandomSource[String](Seq[String]("one", "two", "three"))

  val sourceList: Seq[Source[_, _]] = Seq(gs, bs, ds, yns, ges, des, ri, rd, rc)

  val expectedSamples: Seq[_root_.scala.reflect.runtime.universe.Type] = Seq(
    typeOf[Double],
    typeOf[Boolean],
    typeOf[String],
    typeOf[String],
    typeOf[String],
    typeOf[Int],
    typeOf[Int],
    typeOf[Double],
    typeOf[String]
  )

  val expectedTypes = Seq(
    typeOf[Gaussian],
    typeOf[Bernoulli],
    typeOf[Iterator[LocalDate]],
    typeOf[Bernoulli],
    typeOf[Bernoulli],
    typeOf[Iterator[Int]],
    typeOf[Random],
    typeOf[Random],
    typeOf[Random]
  )
  for (i <- expectedTypes.indices) {
    val source = sourceList(i)
    describe(s"Check ${source.getClass} types") {
      it(s"Should match type tag: ${expectedTypes(i)}") {
        assert(expectedTypes(i) =:= source.S)
      }
      it(s"Sample type should match: ${expectedSamples(i)}") {
        assert(expectedSamples(i) =:= source.V)
      }
    }
  }

  describe("Correct source samples") {
    it("Gaussian should return between 0 and 1"){
      gs.sample() shouldBe (1.0 +- 6.0)
    }
    it("Bernoulli should be True or False"){
      bs.sample() shouldBe a[java.lang.Boolean]
      yns.sample() should (be("Yes") or be("No"))
      ges.sample() should (be("M") or be("F"))
    }
    it("Date source should match format"){
      ds.sample() shouldBe DateTimeFormatter.ISO_DATE.format(LocalDate.now())
    }
  }

}
