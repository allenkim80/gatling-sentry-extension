package com.github.coronoallen.gatling.sentry.actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import com.github.coronoallen.gatling.sentry.constants.{HeaderKey, TagKey}
import com.github.coronoallen.gatling.sentry.message.{HttpFullLog, SendSentry, StringLog}
import com.github.coronoallen.gatling.sentry.utils.CustomLogger

class ParserActor extends Actor with ActorLogging {
  val props = Props(new SentrySendActor)
  var sentrySender: ActorRef = context.actorOf(props)
  context.watch(sentrySender)


  override def receive: Receive = {
    case HttpFullLog(response, message) =>
      val fullLog = CustomLogger.logHttp(response, message)
      val title = CustomLogger.getTitle()
      val username = CustomLogger.getStringFromHeader(response, HeaderKey.USER_NAME)
      val aid = CustomLogger.getStringFromHeader(response, HeaderKey.ACCOUNT_ID)
      val tags:Map[String, String] = Map(
        TagKey.API -> CustomLogger.getAPI(response),
        TagKey.LOG_TYPE -> "fullLog",
        TagKey.USER_NAME -> username,
        TagKey.ACCOUNT_ID -> aid
      )

      sentrySender ! SendSentry(log = fullLog, title = title, tags = tags)
      log.debug("[[[ Full log ]]] {}, {}, {}", fullLog, title, tags)

    case StringLog(username, message) =>
      val tags:Map[String, String] = Map(
        TagKey.LOG_TYPE -> "StringLog",
        TagKey.USER_NAME -> username
      )

      sentrySender ! SendSentry(log = message, title = message, tags = tags)
      log.debug("[[[ String Log ]]] {}, {}", message, tags)

    case Terminated(_) =>
      sentrySender = context.actorOf(props)
      context.watch(sentrySender)
      log.debug("[[[ Sentry sender Actor terminated")
  }
}
