import components.Components
import play.api.Application
import play.api.ApplicationLoader
import play.api.ApplicationLoader.Context
import play.api.LoggerConfigurator
import play.api.Logging

class AppLoader extends ApplicationLoader with Logging {

  def load(context: Context): Application = {

    LoggerConfigurator(context.environment.classLoader).foreach {
      _.configure(context.environment, context.initialConfiguration, Map.empty)
    }

    logger.info("Loading clothing application.")
    new Components(context).application
  }

}
