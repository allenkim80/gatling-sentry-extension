package com.github.coronoallen.gatling.sentry.actor

import akka.actor.{Actor, ActorLogging, ActorSystem, PoisonPill, Props, Terminated}
import akka.routing._
import com.github.coronoallen.gatling.sentry.message.{HttpFullLog, StringLog}
import com.typesafe.config.ConfigFactory

class RouterCreator(system:ActorSystem) extends Actor with ActorLogging {
  val config = ConfigFactory.load()
  val router = system.actorOf(RoundRobinGroup(List()).props(), "router")
  val routeeCount = config.getInt("sentry.actor.routee-count")
  val props = Props(new ParserActor)
  var childInstanceCount = 0


  override def preStart() = {
    super.preStart()
    (0 until routeeCount).map(count => createRoutee())
  }

  def createRoutee() = {
    childInstanceCount += 1
    val child = context.actorOf(props, "router" + childInstanceCount)
    val selection = context.actorSelection(child.path)
    router ! AddRoutee(ActorSelectionRoutee(selection))
    context.watch(child)
  }

  override def receive: Receive = {
    case HttpFullLog(response, message) =>
      log.debug("[[[ Get HttpLog messages ]]]")
      router ! HttpFullLog(response, message)

    case StringLog(username, message) =>
      router ! StringLog(username, message)

    case Terminated(child) =>
      router ! GetRoutees

    case routees: Routees => {
      import collection.JavaConverters._
      val active = routees.getRoutees.asScala.map {
        case x: ActorSelectionRoutee => x.selection.pathString
      }

      for (routee <- context.children) {
        val index = active.indexOf(routee.path.toStringWithoutAddress)
        if (index >= 0) {
          active.remove(index)
        } else {
          routee ! PoisonPill
        }
      }

      for (terminated <- active) {
        val name = terminated.substring(terminated.lastIndexOf("/") + 1)
        val child = context.actorOf(props, name)
        context.watch(child)
      }
    }
  }
}
