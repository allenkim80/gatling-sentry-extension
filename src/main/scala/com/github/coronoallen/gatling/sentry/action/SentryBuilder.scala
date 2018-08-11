package com.github.coronoallen.gatling.sentry.action

import akka.actor.ActorRef
import io.gatling.core.action.Action
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.session.Expression
import io.gatling.core.structure.ScenarioContext
import io.gatling.http.Predef.Response

class SentryHttpLogBuilder(requestName:Expression[String], router: ActorRef, response:Response, message:String) extends ActionBuilder {
  override def build(ctx: ScenarioContext, next: Action): Action = {
    HttpLogSendAction(requestName = requestName, router = router, response = response, message = message, next = next)
  }
}

class SentryStringLogBuilder(requestName:Expression[String], router: ActorRef, username:String, message:String) extends ActionBuilder {
  override def build(ctx: ScenarioContext, next: Action): Action = {
    StringLogSendAction(requestName = requestName, router = router, username = username, message = message, next = next)
  }
}
