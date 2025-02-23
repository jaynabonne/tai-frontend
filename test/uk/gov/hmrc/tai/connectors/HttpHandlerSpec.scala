/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.tai.connectors

import org.joda.time.LocalDate
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.mockito.{Matchers, Mockito}
import org.scalatest.BeforeAndAfterEach
import play.api.http.Status._
import play.api.libs.json.{Format, JsString, Json}
import uk.gov.hmrc.http._
import uk.gov.hmrc.play.bootstrap.http.DefaultHttpClient
import utils.BaseSpec

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class HttpHandlerSpec extends BaseSpec with BeforeAndAfterEach {

  override def beforeEach: Unit =
    Mockito.reset(http)

  protected case class ResponseObject(name: String, age: Int)
  implicit val responseObjectFormat = Json.format[ResponseObject]

  case class DateRequest(date: LocalDate)

  object DateRequest {
    import play.api.libs.json.JodaWrites._
    import play.api.libs.json.JodaReads._
    implicit val formatDateRequest: Format[DateRequest] = Json.format[DateRequest]
  }

  "getFromApi" should {
    "return valid json" when {
      "data is successfully received from the http get call" in {
        val testUrl = "testUrl"
        when(http.GET[HttpResponse](any())(any(), any(), any()))
          .thenReturn(Future.successful(SuccesfulGetResponseWithObject))
        val responseFuture = sut.getFromApi(testUrl)
        val response = Await.result(responseFuture, 5 seconds)
        response mustBe Json.toJson(responseBodyObject)
        verify(http, times(1)).GET(Matchers.eq(testUrl))(any(), any(), any())
      }
    }

    "result in a BadRequest exception" when {
      "when a BadRequest http response is received from the http get call" in {
        when(http.GET[HttpResponse](any())(any(), any(), any())).thenReturn(Future.successful(BadRequestHttpResponse))
        val responseFuture = sut.getFromApi("")
        val ex = the[BadRequestException] thrownBy Await.result(responseFuture, 5 seconds)
        ex.message mustBe "\"bad request\""
      }
    }

    "result in a NotFound exception" when {
      "when a NotFound http response is received from the http get call" in {
        when(http.GET[HttpResponse](any())(any(), any(), any())).thenReturn(Future.successful(NotFoundHttpResponse))
        val responseFuture = sut.getFromApi("")
        val ex = the[NotFoundException] thrownBy Await.result(responseFuture, 5 seconds)
        ex.message mustBe "\"not found\""
      }
    }

    "result in a InternalServerError exception" when {
      "when a InternalServerError http response is received from the http get call" in {
        when(http.GET[HttpResponse](any())(any(), any(), any()))
          .thenReturn(Future.successful(InternalServerErrorHttpResponse))
        val responseFuture = sut.getFromApi("")
        val ex = the[InternalServerException] thrownBy Await.result(responseFuture, 5 seconds)
        ex.message mustBe "\"internal server error\""
      }
    }

    "result in a Locked exception" when {
      "when a Locked response is received from the http get call" in {
        when(http.GET[HttpResponse](any())(any(), any(), any())).thenReturn(Future.successful(LockedHttpResponse))
        val responseFuture = sut.getFromApi("")
        val ex = the[LockedException] thrownBy Await.result(responseFuture, 5 seconds)
        ex.message mustBe "\"locked\""
      }
    }

    "result in an HttpException" when {
      "when a unknown error http response is received from the http get call" in {
        when(http.GET[HttpResponse](any())(any(), any(), any())).thenReturn(Future.successful(UnknownErrorHttpResponse))
        val responseFuture = sut.getFromApi("")
        val ex = the[HttpException] thrownBy Await.result(responseFuture, 5 seconds)
        ex.message mustBe "\"unknown response\""
      }
    }
  }

  "getFromApiV2" should {
    "return valid json" when {
      "data is successfully received from the http get call" in {
        val testUrl = "testUrl"
        when(http.GET[HttpResponse](any())(any(), any(), any()))
          .thenReturn(Future.successful(SuccesfulGetResponseWithObject))
        val responseFuture = sut.getFromApiV2(testUrl)
        val response = Await.result(responseFuture, 5 seconds)
        response mustBe Json.toJson(responseBodyObject)
        verify(http, times(1)).GET(Matchers.eq(testUrl))(any(), any(), any())
      }
    }

    "result in a BadRequest exception" when {
      "when a BadRequest http response is received from the http get call" in {
        when(http.GET[HttpResponse](any())(any(), any(), any())).thenReturn(Future.successful(BadRequestHttpResponse))
        val responseFuture = sut.getFromApiV2("")
        val ex = the[BadRequestException] thrownBy Await.result(responseFuture, 5 seconds)
        ex.message mustBe "\"bad request\""
      }
    }

    "result in a NotFound exception" when {
      "when a NotFound http response is received from the http get call" in {
        when(http.GET[HttpResponse](any())(any(), any(), any())).thenReturn(Future.successful(NotFoundHttpResponse))
        val responseFuture = sut.getFromApiV2("")
        val ex = the[NotFoundException] thrownBy Await.result(responseFuture, 5 seconds)
        ex.message mustBe "\"not found\""
      }
    }

    "result in a InternalServerError exception" when {
      "when a InternalServerError http response is received from the http get call" in {
        when(http.GET[HttpResponse](any())(any(), any(), any()))
          .thenReturn(Future.successful(InternalServerErrorHttpResponse))
        val responseFuture = sut.getFromApiV2("")
        val ex = the[InternalServerException] thrownBy Await.result(responseFuture, 5 seconds)
        ex.message mustBe "\"internal server error\""
      }
    }

    "result in a Locked exception" when {
      "when a Locked response is received from the http get call" in {
        when(http.GET[HttpResponse](any())(any(), any(), any())).thenReturn(Future.successful(LockedHttpResponse))
        val responseFuture = sut.getFromApiV2("")
        val ex = the[LockedException] thrownBy Await.result(responseFuture, 5 seconds)
        ex.message mustBe "\"locked\""
      }
    }

    "result in an HttpException" when {
      "when a unknown error http response is received from the http get call" in {
        when(http.GET[HttpResponse](any())(any(), any(), any())).thenReturn(Future.successful(UnknownErrorHttpResponse))
        val responseFuture = sut.getFromApiV2("")
        val ex = the[HttpException] thrownBy Await.result(responseFuture, 5 seconds)
        ex.message mustBe "\"unknown response\""
      }
    }

    "result in an UnauthorisedException" when {
      "when an Unauthorised response is received from the http get call" in {
        val unauthorisedResponse =
          HttpResponse(UNAUTHORIZED, Some(JsString("unauthorised response")), Map("ETag" -> Seq("34")))

        when(http.GET[HttpResponse](any())(any(), any(), any())).thenReturn(Future.successful(unauthorisedResponse))
        val responseFuture = sut.getFromApiV2("")
        val ex = the[HttpException] thrownBy Await.result(responseFuture, 5 seconds)
        ex.message mustBe "\"unauthorised response\""
      }
    }
  }

  "putToApi" should {
    "return OK" in {
      when(http.PUT[DateRequest, HttpResponse](any(), any(), any())(any(), any(), any(), any()))
        .thenReturn(Future.successful(HttpResponse(OK)))

      val result = Await.result(sut.putToApi[DateRequest]("", DateRequest(LocalDate.now())), 5.seconds)

      result.status mustBe OK

    }

    "return Not Found exception" in {
      when(http.PUT[DateRequest, HttpResponse](any(), any(), any())(any(), any(), any(), any()))
        .thenReturn(Future.successful(HttpResponse(NOT_FOUND)))

      val result = the[NotFoundException] thrownBy Await
        .result(sut.putToApi[DateRequest]("", DateRequest(LocalDate.now())), 5.seconds)

      result.responseCode mustBe NOT_FOUND
    }

    "return Internal Server exception" in {
      when(http.PUT[DateRequest, HttpResponse](any(), any(), any())(any(), any(), any(), any()))
        .thenReturn(Future.successful(HttpResponse(INTERNAL_SERVER_ERROR)))

      val result = the[InternalServerException] thrownBy Await
        .result(sut.putToApi[DateRequest]("", DateRequest(LocalDate.now())), 5.seconds)

      result.responseCode mustBe INTERNAL_SERVER_ERROR
    }

    "return Bad Request exception" in {
      when(http.PUT[DateRequest, HttpResponse](any(), any(), any())(any(), any(), any(), any()))
        .thenReturn(Future.successful(HttpResponse(BAD_REQUEST)))

      val result = the[BadRequestException] thrownBy Await
        .result(sut.putToApi[DateRequest]("", DateRequest(LocalDate.now())), 5.seconds)

      result.responseCode mustBe BAD_REQUEST
    }

    "return Http exception" in {
      when(http.PUT[DateRequest, HttpResponse](any(), any(), any())(any(), any(), any(), any()))
        .thenReturn(Future.successful(HttpResponse(GATEWAY_TIMEOUT)))

      val result = the[HttpException] thrownBy Await
        .result(sut.putToApi[DateRequest]("", DateRequest(LocalDate.now())), 5.seconds)

      result.responseCode mustBe GATEWAY_TIMEOUT
    }
  }

  "postToApi" should {
    val userInput = "userInput"

    "return json which is coming from http post call" in {

      when(http.POST[String, HttpResponse](any(), any(), any())(any(), any(), any(), any()))
        .thenReturn(Future.successful(HttpResponse(OK, Some(Json.toJson(userInput)))))
        .thenReturn(Future.successful(HttpResponse(CREATED, Some(Json.toJson(userInput)))))

      val okResponse = Await.result(sut.postToApi[String](mockUrl, userInput), 5 seconds)
      val createdResponse = Await.result(sut.postToApi[String](mockUrl, userInput), 5 seconds)

      okResponse.status mustBe OK
      okResponse.json mustBe Json.toJson(userInput)

      createdResponse.status mustBe CREATED
      createdResponse.json mustBe Json.toJson(userInput)
    }

    "return Http exception" when {
      "http response is NOT_FOUND" in {
        when(http.POST[String, HttpResponse](any(), any(), any())(any(), any(), any(), any()))
          .thenReturn(Future.successful(HttpResponse(NOT_FOUND)))

        val result = the[HttpException] thrownBy Await.result(sut.postToApi[String](mockUrl, userInput), 5 seconds)

        result.responseCode mustBe NOT_FOUND
      }

      "http response is GATEWAY_TIMEOUT" in {
        when(http.POST[String, HttpResponse](any(), any(), any())(any(), any(), any(), any()))
          .thenReturn(Future.successful(HttpResponse(GATEWAY_TIMEOUT)))

        val result = the[HttpException] thrownBy Await.result(sut.postToApi[String](mockUrl, userInput), 5 seconds)

        result.responseCode mustBe GATEWAY_TIMEOUT
      }
    }
  }

  "deleteFromApi" must {
    "post request to DELETE and return the http response" when {
      "http DELETE returns OK" in {

        when(http.DELETE[HttpResponse](any(), any())(any(), any(), any()))
          .thenReturn(Future.successful(HttpResponse(OK)))
        val result = Await.result(sut.deleteFromApi(mockUrl), 5 seconds)
        result.status mustBe OK
      }

      "http DELETE returns NO_CONTENT" in {

        when(http.DELETE[HttpResponse](any(), any())(any(), any(), any()))
          .thenReturn(Future.successful(HttpResponse(NO_CONTENT)))
        val result = Await.result(sut.deleteFromApi(mockUrl), 5 seconds)
        result.status mustBe NO_CONTENT
      }

      "http DELETE returns ACCEPTED" in {
        when(http.DELETE[HttpResponse](any(), any())(any(), any(), any()))
          .thenReturn(Future.successful(HttpResponse(ACCEPTED)))
        val result = Await.result(sut.deleteFromApi(mockUrl), 5.seconds)
        result.status mustBe ACCEPTED
      }

    }
    "return Http exception" when {
      "http response is NOT OK" in {
        when(http.DELETE[HttpResponse](any(), any())(any(), any(), any()))
          .thenReturn(Future.successful(HttpResponse(GATEWAY_TIMEOUT)))

        val result = the[HttpException] thrownBy Await.result(sut.deleteFromApi(mockUrl), 5 seconds)

        result.responseCode mustBe GATEWAY_TIMEOUT
      }
    }
  }

  private val mockUrl = "mockUrl"

  private val responseBodyObject = ResponseObject("Name", 24)

  private val SuccesfulGetResponseWithObject: HttpResponse =
    HttpResponse(OK, Some(Json.toJson(responseBodyObject)), Map("ETag" -> Seq("34")))
  private val BadRequestHttpResponse =
    HttpResponse(BAD_REQUEST, Some(JsString("bad request")), Map("ETag" -> Seq("34")))
  private val NotFoundHttpResponse: HttpResponse =
    HttpResponse(NOT_FOUND, Some(JsString("not found")), Map("ETag" -> Seq("34")))
  private val LockedHttpResponse: HttpResponse =
    HttpResponse(LOCKED, Some(JsString("locked")), Map("ETag" -> Seq("34")))
  private val InternalServerErrorHttpResponse: HttpResponse =
    HttpResponse(INTERNAL_SERVER_ERROR, Some(JsString("internal server error")), Map("ETag" -> Seq("34")))
  private val UnknownErrorHttpResponse: HttpResponse =
    HttpResponse(418, Some(JsString("unknown response")), Map("ETag" -> Seq("34")))

  val http = mock[DefaultHttpClient]

  def sut = new HttpHandler(http)
}
