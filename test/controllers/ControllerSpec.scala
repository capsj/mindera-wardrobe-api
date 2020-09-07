package controllers

import components.WritableInstances
import models.data.DataService
import org.scalamock.scalatest.MockFactory
import org.scalatest.Suite
import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers
import org.scalatest.Inside
import org.scalatestplus.play.components.OneAppPerSuiteWithComponents
import play.api.ApplicationLoader.Context

trait ControllerSpec
  extends Suite
  with AnyFreeSpecLike
  with OneAppPerSuiteWithComponents
  with Inside
  with Matchers
  with MockFactory
  with WritableInstances {

  lazy val dataService = mock[DataService]

  class StubWebApiComponents(context: Context) extends TestComponents(context) {
    override lazy val dataService: DataService = ControllerSpec.this.dataService
  }

  override lazy val components = new StubWebApiComponents(context)
}
