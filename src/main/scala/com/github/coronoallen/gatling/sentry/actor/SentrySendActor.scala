package com.github.coronoallen.gatling.sentry.actor

import akka.actor.{Actor, ActorLogging}
import com.github.coronoallen.gatling.sentry.message.SendSentry
import io.sentry.context.Context
import io.sentry.event.{EventBuilder, UserBuilder}
import io.sentry.{SentryClient, SentryClientFactory}

class SentrySendActor extends Actor with ActorLogging {
  var sentryClient:SentryClient = _
  var ctx:Context = _
  var eventBuilder:EventBuilder = _

  override def preStart(): Unit = {
    super.preStart()
    sentryClient = SentryClientFactory.sentryClient()
    ctx = sentryClient.getContext
    ctx.setUser(new UserBuilder().setUsername("loadtest").setEmail("fo4@ea.com").build())
  }

  override def receive: Receive = {
    case SendSentry(message, title, tags) =>
      eventBuilder = new EventBuilder()

      if (tags.nonEmpty) {
        tags.foreach {
          case (key:String, value:String) =>
            eventBuilder.withTag(key, value)
            log.debug("[[[ Sentry tag log ]]] {}, {}", key, value)
        }
      }
      eventBuilder.withFingerprint(title).withMessage(message)
      sentryClient.sendEvent(eventBuilder)
      log.debug("[[[ Sentry actor log ]]] {}, {}", message, tags)
  }
}
