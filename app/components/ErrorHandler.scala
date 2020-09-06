package components

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import models.CustomError
import models.ErrorResponseBody
import models.WritableInstances
import play.api.http.HttpErrorHandlerExceptions
import play.api.http.JsonHttpErrorHandler
import play.api.mvc.Results._
import play.api.mvc.RequestHeader
import play.api.mvc.Result
import play.api.Environment
import play.api.Logging
import play.api.Mode

class ErrorHandler(environment: Environment)(implicit ec: ExecutionContext)
  extends JsonHttpErrorHandler(environment, None)
    with WritableInstances
    with Logging {

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] =
    Future {
      val isProd          = environment.mode == Mode.Prod
      val usefulException = HttpErrorHandlerExceptions.throwableToUsefulException(None, isProd, exception)
      logServerError(request, usefulException)

      (isProd, exception) match {
        case (_, err: CustomError) => err.defaultHttpStatus(ErrorResponseBody(request.id, err.message))
        case (true, _)             => InternalServerError(ErrorResponseBody(request.id, usefulException.description))
        case (false, _)            => InternalServerError(super.devServerError(request, usefulException))
      }
    }
}
