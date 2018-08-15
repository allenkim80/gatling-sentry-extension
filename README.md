# gatling-sentry-extension

`gatling-sentry-extension` is the one that can easily send gatling logs to sentry. Gatling is the powerful tool to check application's performance, but it is hard to check what kind of errors occur during the test. So, this extension help you check error logs with Sentry.(https://sentry.io/welcome/)
This extension is made based on Akka framework, when the start gatling test, it should make Actor system for `gatling-sentry-extension`. I will explain more detaily below section.

This extension can send those logs to Sentry.
  - Gatling http response
  - All string logs that you want


### Installation

If you want to use, need to add the dependency in your `build.sbt`

```
"com.github.allenkim80" % "gatling-sentry-extension_2.11" % "0.1.16-SNAPSHOT"
```
`Now you can use snapshot version, as soon as possible I will publish release version.`

### Usage

### Create actor system for sentry-extension

```
// You need to make gatling simulation project.

class SetnryTestSimulation extends Simulation {

  // Start extension
  startSentry()

  val scn = scenario("Sentry")
    .exec(
      http("/endpoint")
        .get("/endpoint")
        .transformResponse {case response if response.isReceived => sendSentryLog(response, List("Error1", "message")}  
    ).exec()
    .exec(sentry("").sendStringLogByAction("test2", "message"))
    .pause(3)
 
 def createHttpProtocol() : HttpProtocolBuilder = {
    http
      .baseURL("http://server-address")
      .acceptHeader("*/*")
      .headers(Map(
        "Content-Type" -> "application/json"
      ))
      .disableWarmUp
  }
 
 def sendSentryLog(response: Response, validErrors:List[String], message:String = "") = {
    val resultObject = new JsonParser().parse(response.body.string).getAsJsonObject.get("result")

    resultObject match {
      case result if result != null && result.getAsString == "true" => /* do nothing */
      case _ =>
        val reasonObject = new JsonParser().parse(response.body.string).getAsJsonObject.get("reason")

        if (!validErrors.contains(reasonObject.getAsString)) {
          sentry("").sendHttpLog(response)
        }
    }
    response
  }

  // start gatling simulation
  setUp(scn.inject(rampUsers(2) over (1 minute))).protocols(createHttpProtocol())

  // stop extension
  stopSentry()

}
```

