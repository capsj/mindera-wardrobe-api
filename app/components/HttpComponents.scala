package components

import play.api.BuiltInComponents
import play.api.mvc.EssentialFilter
import play.filters.HttpFiltersComponents
import play.filters.cors.CORSComponents

trait HttpComponents extends HttpFiltersComponents with CORSComponents { this: BuiltInComponents =>

//  lazy val loggingFilter = new AccessLoggingFilter()(materializer, executionContext)

  /** Make the received requests go through this list of Filters */
  override val httpFilters: Seq[EssentialFilter] = /* loggingFilter +: */ corsFilter +: super.httpFilters

  /** Failures should go through our custom handler */
  override lazy val httpErrorHandler = new ErrorHandler(environment)
}

