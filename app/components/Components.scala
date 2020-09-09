package components

import com.softwaremill.macwire.wire
import com.typesafe.config.Config
import controllers.AssetsComponents
import controllers.ClothingController
import controllers.CsvReader
import controllers.CsvReaderImpl
import data.DataRepository
import data.DataService
import data.DataServiceImpl
import play.api.ApplicationLoader.Context
import play.api.BuiltInComponentsFromContext
import play.api.mvc.EssentialFilter
import play.api.routing.Router
import router.Routes

class Components(context: Context)
  extends BuiltInComponentsFromContext(context)
  with SlickDatabaseComponents
  with AssetsComponents {

  override def httpFilters: Seq[EssentialFilter] = Seq.empty

  lazy val config: Config       = configuration.underlying
  lazy val csvReader: CsvReader = wire[CsvReaderImpl]
  lazy val dataRepository: DataRepository = new DataRepository {
    override val database = Components.this.database
  }
  lazy val dataService: DataService = wire[DataServiceImpl]

  lazy val clothingController = wire[ClothingController]
  lazy val router: Router = {
    // add the prefix string in local scope for the Routes constructor
    val prefix: String = "/"
    wire[Routes]
  }

  migrateDatabase
}
