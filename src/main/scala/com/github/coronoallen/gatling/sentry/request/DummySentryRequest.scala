package com.github.coronoallen.gatling.sentry.request

import io.gatling.core.action.Action
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.session.Session
import io.gatling.core.structure.ScenarioContext
import io.gatling.http.response.Response

class DummySentryRequest extends BaseSentryRequest {
  override def sendHttpLogByAction(response: Response, message: String = ""): ActionBuilder = {
    new DummyActionBuilder
  }

  override def sendStringLogByAction(username:String, message: String = ""): ActionBuilder = {
    new DummyActionBuilder
  }

  override def sendHttpLog(response: Response, message: String = "") = {}

  override def sendStrLog(username:String, message: String = "") = {}
}

class DummyActionBuilder extends ActionBuilder {
  override def build(ctx: ScenarioContext, next: Action): Action = new DummyAction(next)
}

class DummyAction(next:Action) extends Action {
  override def name = "dummy"

  override def execute(session: Session): Unit = {
    next ! session
  }
}