package com.github.coronoallen.gatling.sentry.message

import io.gatling.http.Predef.Response

case class HttpFullLog(
                        response: Response = null,
                        message: String = ""
                      )

case class StringLog(
                      username: String = "",
                      message: String = ""
                    )

case class SendSentry(
                     log:String = "",
                     title:String = "",
                     tags:Map[String, String] = Map.empty[String, String]
                     )
