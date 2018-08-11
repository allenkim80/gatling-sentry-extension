package com.github.coronoallen.gatling.sentry.request

import akka.actor.ActorRef
import com.github.coronoallen.gatling.sentry.action.{SentryHttpLogBuilder, SentryStringLogBuilder}
import com.github.coronoallen.gatling.sentry.message.{HttpFullLog, StringLog}
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.session._
import io.gatling.http.response.Response

class SentryRequest(requestName: Expression[String], router:ActorRef) extends BaseSentryRequest {
  override def sendHttpLogByAction(response: Response, message: String = ""): ActionBuilder = {
    new SentryHttpLogBuilder(requestName = requestName, router = router, response = response, message = message)
  }

  override def sendStringLogByAction(username:String, message: String = ""): ActionBuilder = {
    new SentryStringLogBuilder(requestName = requestName, router = router, username = username, message = message)
  }

  override def sendHttpLog(response: Response, message: String = "") = {
    router ! HttpFullLog(response = response, message = message)
  }

  override def sendStrLog(username:String, message: String = "") = {
    router ! StringLog(username = username, message = message)
  }
}
