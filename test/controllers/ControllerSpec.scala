package controllers

import components.WritableInstances
import models.data.DataService
import org.scalamock.scalatest.MockFactory
import org.scalatest.Suite
import org.scalatest.matchers.should.Matchers
import org.scalatest.Inside
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatestplus.play.components.OneAppPerSuiteWithComponents
import play.api.ApplicationLoader.Context

trait ControllerSpec
  extends Suite
  with AnyFlatSpecLike
  with OneAppPerSuiteWithComponents
  with Inside
  with Matchers
  with MockFactory
  with WritableInstances {

  lazy val dataService = mock[DataService]
  lazy val csvReader = mock[CsvReader]

  class StubWebApiComponents(context: Context) extends TestComponents(context) {
    override lazy val dataService: DataService = ControllerSpec.this.dataService
    override lazy val csvReader: CsvReader = ControllerSpec.this.csvReader
  }

  override lazy val components = new StubWebApiComponents(context)
}
