package com.github.coronoallen.gatling.sentry.action

import akka.actor.ActorRef
import com.github.coronoallen.gatling.sentry.message.HttpFullLog
import io.gatling.core.action.Action
import io.gatling.core.session.{Expression, Session}
import io.gatling.http.Predef.Response

case class HttpLogSendAction(requestName: Expression[String], router: ActorRef, response: Response, message:String, next:Action) extends Action {
  override def name: String = "sendHttpFullLog"

  override def execute(session: Session): Unit = {
    router ! HttpFullLog(response = response, message = message)
    next ! session
  }
}
