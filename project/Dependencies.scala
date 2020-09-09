import sbt._

object Version {
  val akka           = "2.6.8"
  val cats           = "2.1.1"
  val flyway         = "6.5.4"
  val macwire        = "2.3.7"
  val postgresDriver = "42.2.5"
  val playJson       = "2.8.1"
  val playScalaTest  = "5.1.0"
  val scalaTest      = "3.2.0"
  val slick          = "3.3.2"
  val slickPg        = "0.19.2"
  val typeSafeConfig = "1.4.0"
}

object Library {

  val akkaStream = "com.typesafe.akka" %% "akka-stream" % Version.akka
  val cats       = "org.typelevel"     %% "cats-core"   % Version.cats
  val chimney    = "io.scalaland"      %% "chimney"     % "0.5.3"
  val playJson   = "com.typesafe.play" %% "play-json"   % Version.playJson

  // DI
  val macwire = "com.softwaremill.macwire" %% "macros" % Version.macwire % Provided

  // Config
  val typeSafeConfig = "com.typesafe" % "config" % Version.typeSafeConfig

  // DB
  val slick       = "com.typesafe.slick"  %% "slick"              % Version.slick
  val flyway      = "org.flywaydb"         % "flyway-core"        % Version.flyway
  val postgres    = "org.postgresql"       % "postgresql"         % Version.postgresDriver
  val playSlick   = "com.typesafe.play"   %% "play-slick"         % "5.0.0"
  val slickPg     = "com.github.tminglei" %% "slick-pg"           % Version.slickPg
  val slickPgJson = "com.github.tminglei" %% "slick-pg_play-json" % Version.slickPg

  // Test
  val scalaTest          = "org.scalatest"          %% "scalatest"          % "3.2.1"               % Test
  val playScalaTest      = "org.scalatestplus.play" %% "scalatestplus-play" % Version.playScalaTest % Test
  val scalaMock          = "org.scalamock"          %% "scalamock"          % "5.0.0"               % Test

  // Csv
  val kantan = "com.nrinaudo" %% "kantan.csv" % "0.6.0"
}
