package com.github.coronoallen.gatling.sentry.utils

import com.github.coronoallen.gatling.sentry.constants.{HeaderKey, ResponseKey, StatusCode}
import com.google.gson.{GsonBuilder, JsonParser}
import io.gatling.http.Predef.{Request, Response}
import io.netty.handler.codec.http.HttpHeaders
import org.slf4j.MarkerFactory

object CustomLogger {
  val gson = new GsonBuilder().setPrettyPrinting().create()
  val MARKER = MarkerFactory.getMarker("CustomMarker")
  var title:String = _
  val excludedHeaders = List(
    HeaderKey.USER_NAME,
    HeaderKey.CONTENT_LENGTH,
    HeaderKey.AUTHORIZATION,
    HeaderKey.ACCOUNT_ID,
    HeaderKey.DATE
  )

  def logHttp(response: Response, message: String = "") = {
    val failReason = response.statusCode.getOrElse(0) match {
      case status if StatusCode.SuccessCodes.contains(status) =>
        getFailReason(response)
      case statusCode => s"status $statusCode"
    }

    val title = createTitle(response.request.getMethod, response.request.getUri.getPath, failReason, message)
    val requestLog = createRequestLog(response.request)
    val responseLog = createResponseLog(response)
    val fullLog = createFullLog(title, requestLog, responseLog)

    fullLog
  }

  def createTitle(method: String, path: String, reason:String, message: String): String = {
    title = s"$method $path | $reason | $message"
    title
  }

  def createFullLog(title: String, requestLog: String, responseLog: String): String = {
    s"""
       |$title
       |====================================
       |$requestLog
       |====================================
       |$responseLog
       |>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>""".stripMargin
  }

  def createRequestLog(request: Request) = {
    s"""Http Request :
       |${request.getMethod} ${request.getUri}
       |headers = ${httpHeadersToString(request.getHeaders)}
       |body = ${getStringToJson(request.getStringData)}""".stripMargin
  }

  def createResponseLog(response: Response) = {
    s"""Http Response :
       |headers = ${httpHeadersToString(response.headers)}
       |body = ${getStringToJson(response.body.string)}""".stripMargin
  }

  def httpHeadersToString(headers: HttpHeaders): String = {
    val builder = StringBuilder.newBuilder
    val iterator = headers.names().iterator()

    while (iterator.hasNext) {
      val name = iterator.next()

      if (!excludedHeaders.contains(name)) {
        builder.append("\n")
          .append(name)
          .append(" : ")
          .append(headers.get(name))
      }
    }

    builder.toString()
  }

  def getAPI(response: Response) = {
    s"${response.request.getMethod} ${response.request.getUri.getPath}"
  }

  def getStringToJson(string:String): String = {
    gson.toJson(string)
  }

  def getFailReason(response:Response): String = {
    val reasonObject = new JsonParser().parse(response.body.string).getAsJsonObject.get(ResponseKey.REASON)
    if (reasonObject != null) {
      reasonObject.getAsString
    } else {
      ""
    }
  }

  def getStringFromHeader(response: Response, key: String): String = {
    val username = response.request.getHeaders.get(key)
    if (username != null) {
      username
    } else {
      ""
    }
  }

  def getTitle(): String = title
}
