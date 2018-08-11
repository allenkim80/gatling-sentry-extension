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
  val nexus = "http://apseo-nexus"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "/repository/maven-snapshots")
  else
    Some("releases"  at nexus + "/repository/maven-releases")
}

credentials += Credentials( "Sonatype Nexus Repository Manager", "apseo-nexus", "eass-build", "Welcome2ea!!")

pomIncludeRepository := { _ => false }

pomExtra := <url>http://git.sphd.io/qe/gatling-sentry-extension</url>
  <licenses>
    <license>
      <name>The MIT License (MIT)</name>
      <url>https://opensource.org/licenses/MIT</url>
    </license>
  </licenses>
  <scm>
    <url>http://git.sphd.io/qe/gatling-sentry-extension.git</url>
    <connection>scm:git:http://git.sphd.io/qe/gatling-sentry-extension.git</connection>
  </scm>
  <developers>
    <developer>
      <id>seunkim</id>
      <name>Allen Kim</name>
    </developer>
  </developers>