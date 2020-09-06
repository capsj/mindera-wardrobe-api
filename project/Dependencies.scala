import sbt._

object Version {
  val akka               = "2.6.8"
  val cats               = "2.1.1"
  val enumeratum         = "1.6.1"
  val enumeratumPlayJson = "1.6.1"
  val logback            = "1.2.3"
  val flyway             = "6.5.4"
  val macwire            = "2.3.7"
  val postgresDriver     = "42.2.5"
  val playJson           = "2.8.1"
  val playScalaTest      = "5.1.0"
  val refined            = "0.9.15"
  val scalaTest          = "3.2.0"
  val slick              = "3.3.2"
  val typeSafeConfig     = "1.4.0"
}

object Library {

  // Json
  val playJson       = "com.typesafe.play" %% "play-json"        % Version.playJson
//  val playJsonTraits = "io.leonard"        %% "play-json-traits" % "1.5.1"

  // Logging
  val logback         = "ch.qos.logback"       % "logback-classic"          % Version.logback
  val logstashEncoder = "net.logstash.logback" % "logstash-logback-encoder" % "6.3"

  // DI
  val macwire     = "com.softwaremill.macwire" %% "macros"     % Version.macwire % Provided
//  val macwireAkka = "com.softwaremill.macwire" %% "macrosakka" % Version.macwire % Provided

  // Config
  val typeSafeConfig = "com.typesafe" % "config" % Version.typeSafeConfig

  // Chimney
  val chimney = "io.scalaland" %% "chimney" % "0.5.3"

  // DB
  val slick     = "com.typesafe.slick" %% "slick"                % Version.slick
  val flyway    = "org.flywaydb"        % "flyway-core"          % Version.flyway
  val postgres  = "org.postgresql" % "postgresql" % Version.postgresDriver
  val playSlick = "com.typesafe.play"  %% "play-slick"           % "5.0.0"

  // Optics
//  val quicklens = "com.softwaremill.quicklens"  % "quicklens_2.11" % "1.6.1"
//  val monocle   = "com.github.julien-truffaut" %% "monocle-core"   % "2.0.4"

  // Test
  val scalaTest          = "org.scalatest"          %% "scalatest"                   % "3.2.1"               % Test
  val playScalaTest      = "org.scalatestplus.play" %% "scalatestplus-play"          % Version.playScalaTest % Test
  val scalaMock          = "org.scalamock"          %% "scalamock"                   % "5.0.0"               % Test
  val scalaCheck         = "org.scalacheck"         %% "scalacheck"                  % "1.14.3"              % Test
  val scalaTestPlusCheck = "org.scalatestplus"      %% "scalacheck-1-14"             % "3.2.1.0"             % Test

}
