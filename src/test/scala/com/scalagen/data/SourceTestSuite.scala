package com.scalagen.data

import breeze.stats.distributions.{Bernoulli, Gaussian}
import com.scalagen.data.api.Source
import org.scalatest._

import scala.collection.immutable.StreamIterator

class SourceTestSuite extends FunSpec with Matchers {

  val gs: GaussianSource  = GaussianSource(1.0, 2.0)
  val bs: BernoulliSource = BernoulliSource()
  val ds: DateSource      = DateSource("2015-10-31")
  val yns: YesNoSource    = YesNoSource()
  val ges: GenderSource   = GenderSource()

  val sourceList: Seq[Source[_, _]]  = Seq(gs, bs, ds, yns, ges)
  val expectedSources: Seq[Class[_]] = Seq(classOf[Gaussian], classOf[Bernoulli], classOf[StreamIterator[_]], classOf[Bernoulli], classOf[Bernoulli])
  val expectedSamples                = Seq(classOf[java.lang.Double], classOf[java.lang.Boolean], classOf[String], classOf[String], classOf[String])

  for (i <- sourceList.indices) {
    val source: Source[_, _]     = sourceList(i)
    val expected: Class[_]       = expectedSources(i)
    val expectedSample: Class[_] = expectedSamples(i)
    val classDef: String         = source.getClass.getCanonicalName

    describe(s"Class Types for: $classDef") {
      it("Should be the correct source type") {
        source.source.getClass should equal(expected)
      }
      it("Should be the correct sample type") {
        source.sample().getClass shouldBe an[expectedSample.type]
      }
    }
  }
}
