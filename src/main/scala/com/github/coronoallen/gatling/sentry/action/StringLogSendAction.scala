package com.github.coronoallen.gatling.sentry.action

import akka.actor.ActorRef
import com.github.coronoallen.gatling.sentry.message.StringLog
import io.gatling.core.action.Action
import io.gatling.core.session.{Expression, Session}

case class StringLogSendAction(requestName: Expression[String], router: ActorRef, username:String, message:String, next:Action) extends Action {
  override def name: String = "sendStringLog"

  override def execute(session: Session): Unit = {
    router ! StringLog(username = username, message = message)
    next ! session
  }
}
