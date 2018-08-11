package com.github.coronoallen.gatling.sentry.request

import io.gatling.core.action.builder.ActionBuilder
import io.gatling.http.response.Response

trait BaseSentryRequest {
  def sendHttpLogByAction(response: Response, message: String = ""): ActionBuilder

  def sendStringLogByAction(username:String, message: String = ""): ActionBuilder

  def sendHttpLog(response: Response, message: String = ""):Unit

  def sendStrLog(username:String, message: String = ""):Unit
}

