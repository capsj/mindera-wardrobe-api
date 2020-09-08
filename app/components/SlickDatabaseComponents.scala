package components

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.Await
import scala.util.Failure
import scala.util.Success
import scala.util.Try

import org.flywaydb.core.Flyway
import play.api.Logging
import play.api.db.slick.DbName
import play.api.BuiltInComponents
import play.api.db.slick.SlickComponents
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

trait DatabaseComponents {
  def database: JdbcProfile#Backend#Database
  def profile: JdbcProfile
}

trait SlickDatabaseComponents extends DatabaseComponents with SlickComponents with Logging { this: BuiltInComponents =>

  lazy val slickConfig: DatabaseConfig[JdbcProfile] = slickApi.dbConfig[JdbcProfile](DbName("default"))
  override lazy val database = slickConfig.db
  override lazy val profile = slickConfig.profile

  private val dbConfig = slickConfig.config.getConfig("db")
  private val databaseUrl = dbConfig.getString("url")
  private val databaseUsername = dbConfig.getString("username")
  private val databasePassword = dbConfig.getString("password")
  private val migrationsLocation = "db/migration"

  lazy val cleanDb = Try(dbConfig.getBoolean("clean")).getOrElse(false)

  final def migrateDatabase(doClean: Boolean = true): Unit =
    Try(performMigration(doClean)) match {
      case Success(migrationsApplied: Int) =>
        logger.info(s"Migration completed successfully: applied $migrationsApplied migrations")
        ()
      case Failure(ex) =>
        logger.error("Stopping application")
        logger.error("Error during migration.", ex)
        Await.result(application.stop(), FiniteDuration(10, "s"))
    }

  private def performMigration(doClean: Boolean): Int = {
    val flyway = Flyway
      .configure()
      .locations(Seq(migrationsLocation): _*)
      .dataSource(databaseUrl, databaseUsername, databasePassword)
      .cleanDisabled(!doClean)
      .load()

    logger.info(s"Flyway migration intiating for $databaseUrl...")

    flyway.info().all().foreach(i => logger.info(s"${i.getVersion} ${i.getDescription} : ${i.getState}"))

    if (doClean) {
      logger.info("Performing flyway clean...")
      flyway.clean()
    }

    flyway.migrate()
  }
}
