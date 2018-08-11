import io.gatling.sbt.GatlingPlugin

val scala_version   = "2.11.8"
val akkaVersion     = "2.5.12"
val gatlingVersion  = "2.2.5"

lazy val root = (project in file("."))
  .enablePlugins(GatlingPlugin)
  .settings(
    name := "gatling-sentry-extension",
    version := (version in ThisBuild).value,
    organization := "com.ea.spearhead",
    homepage := Some(url("https://github.com/allenkim80/gatling-sentry-extension")),
    scmInfo := Some(ScmInfo(url("https://github.com/allenkim80/gatling-sentry-extension"),
                                "git@github.com:allenkim80/gatling-sentry-extension.git")),
    developers := List(Developer("allenkim80",
      "Allen Kim",
      "corono1004@gmail.com",
      url("https://github.com/allenkim80"))),
    licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0")),
    scalaVersion := scala_version,
    libraryDependencies ++= {
      Seq(
        "com.typesafe.akka"   %% "akka-actor"                 % akkaVersion,
        "com.typesafe.akka"   %% "akka-stream"                % akkaVersion,
        "com.typesafe.akka"   %% "akka-slf4j"                 % akkaVersion,
        "ch.qos.logback"      %  "logback-classic"           % "1.2.3",

        "com.typesafe.akka"   %% "akka-testkit"               % akkaVersion     % "test",

        "org.scala-lang"      % "scala-library"              % scala_version,
        "org.scalatest"       %% "scalatest"                  % "3.0.5"         % "test",

        "com.typesafe.akka"   %% "akka-remote"                % akkaVersion,
        "com.typesafe.akka"   %% "akka-multi-node-testkit"    % akkaVersion     % "test",

        "io.gatling"          % "gatling-core"                % gatlingVersion,
        "io.gatling"          % "gatling-http"                % gatlingVersion,
        "io.gatling"          % "gatling-test-framework"      % gatlingVersion  % "test",
        "io.gatling.highcharts" % "gatling-charts-highcharts"  % gatlingVersion  % "test",

        "io.sentry"             % "sentry"                      % "1.7.5",
        "com.google.code.gson" % "gson"                       % "2.8.5"
      )
    }
  )

publishArtifact in Test := false

publishArtifact in packageBin := true

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomIncludeRepository := { _ => false }