package com.github.coronoallen.gatling.sentry

import akka.actor.{ActorSystem, Props}
import akka.routing.{ActorSelectionRoutee, AddRoutee, RoundRobinGroup}
import akka.testkit.{TestKit, TestProbe}
import com.github.coronoallen.gatling.sentry.actor.{ParserActor, RouterCreator}
import com.github.coronoallen.gatling.sentry.message.StringLog
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}

class SentryTest
  extends TestKit(ActorSystem("SentryTest"))
  with WordSpecLike
  with MustMatchers
  with BeforeAndAfterAll
{
  override def afterAll(): Unit = {
    system.terminate()
  }

  "Parser Actor " must {
    "get message from router with direct path" in {
      val testActor = TestProbe()

      val router = system.actorOf(RoundRobinGroup(List(testActor.ref.path.toStringWithoutAddress)).props(), "router1")

      val msg = StringLog("test log")
      router ! msg
      testActor.expectMsg(msg)
    }

    "get message from router with creator" in {


      val router = system.actorOf(RoundRobinGroup(List()).props(), "router2")
      val props = Props(new ParserActor)
      var childInstanceCount = 0

      (0 until 1).map(count => createRoutee())

      def createRoutee() = {
        childInstanceCount += 1
        val child = system.actorOf(props, "router1" + childInstanceCount)
        val selection = system.actorSelection(child.path)
        router ! AddRoutee(ActorSelectionRoutee(selection))
      }

      router ! StringLog("test")
    }

    "get message from router with creator function" in {
      val routerCreator = system.actorOf(Props(new RouterCreator(system)), "routerCreator")

      Thread.sleep(1000)

      routerCreator ! StringLog("test3")
    }
  }
}
