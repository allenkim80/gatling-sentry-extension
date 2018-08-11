package com.github.coronoallen.gatling.sentry

import akka.actor.{ActorRef, ActorSystem, Props}
import com.github.coronoallen.gatling.sentry.actor.RouterCreator
import com.github.coronoallen.gatling.sentry.request.{DummySentryRequest, SentryRequest}
import com.typesafe.config.ConfigFactory
import io.gatling.core.session.Expression
import io.sentry.Sentry

object Predef {
  var system:ActorSystem = _
  var router: ActorRef = _
  val enable = ConfigFactory.load().getBoolean("sentry.enable")

  def sentry(requestName: Expression[String]) = {
    if (enable) new SentryRequest(requestName, router)
    else new DummySentryRequest
  }

  def startSentry() = if (enable) {
    system = ActorSystem("SentryActorSystem")
    Sentry.init()
    router = system.actorOf(Props(new RouterCreator(system)))
  }

  def stopSentry() = if (enable) {
    Sentry.close()
    system.terminate()
  }
}
