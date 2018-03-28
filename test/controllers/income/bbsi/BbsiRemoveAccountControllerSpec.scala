/*
 * Copyright 2018 HM Revenue & Customs
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

package controllers.income.bbsi

import builders.{AuthBuilder, RequestBuilder}
import controllers.FakeTaiPlayApplication
import mocks.MockTemplateRenderer
import org.jsoup.Jsoup
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.test.Helpers._
import uk.gov.hmrc.tai.service.{BbsiService, TaiService}
import uk.gov.hmrc.tai.model.domain.BankAccount
import uk.gov.hmrc.play.frontend.auth.connectors.domain._
import uk.gov.hmrc.play.frontend.auth.connectors.{AuthConnector, DelegationConnector}
import uk.gov.hmrc.play.partials.PartialRetriever
import uk.gov.hmrc.renderer.TemplateRenderer
import uk.gov.hmrc.tai.model.TaiRoot

import scala.concurrent.Future

class BbsiRemoveAccountControllerSpec extends PlaySpec with MockitoSugar with FakeTaiPlayApplication with I18nSupport {

  implicit val messagesApi: MessagesApi = app.injector.instanceOf[MessagesApi]

  "Bbsi Remove controller" must {
    "display remove confirmation view" in {
      val sut = createSut
      when(sut.bbsiService.bankAccount(any(), any())(any())).thenReturn(Future.successful(Some(bankAccount)))

      val result = sut.checkYourAnswers(1)(RequestBuilder.buildFakeRequestWithAuth("GET"))

      status(result) mustBe OK
      val doc = Jsoup.parse(contentAsString(result))
      doc.title() must include(Messages("tai.bbsi.remove.checkYourAnswers.title", bankAccount.bankName.getOrElse("")))
    }

    "return not found" when {
      "account not found" in {
        val sut = createSut
        when(sut.bbsiService.bankAccount(any(), any())(any())).thenReturn(Future.successful(None))

        val result = sut.checkYourAnswers(1)(RequestBuilder.buildFakeRequestWithAuth("GET"))

        status(result) mustBe NOT_FOUND
      }
    }

    "redirect to confirmation page" when {
      "we received envelope-id successfully" in {
        val sut = createSut
        when(sut.bbsiService.removeBankAccount(any(), any())(any())).thenReturn(Future.successful("123-456-789"))

        val result = sut.submitYourAnswers(1)(RequestBuilder.buildFakeInvalidRequestWithAuth("POST"))

        status(result) mustBe SEE_OTHER
        redirectLocation(result).get mustBe controllers.income.bbsi.routes.BbsiController.removeConfirmation().url
      }
    }
  }

  private val bankAccount = BankAccount(1, Some("****5678"), Some("123456"), Some("Test Bank account name"), 100, None)

  def createSut = new SUT

  class SUT extends BbsiRemoveAccountController {
    override val taiService: TaiService = mock[TaiService]
    override val bbsiService: BbsiService = mock[BbsiService]
    override implicit val templateRenderer: TemplateRenderer = MockTemplateRenderer
    override implicit val partialRetriever: PartialRetriever = mock[PartialRetriever]
    override protected val authConnector: AuthConnector = mock[AuthConnector]
    override protected val delegationConnector: DelegationConnector = mock[DelegationConnector]

    val ad: Future[Some[Authority]] = AuthBuilder.createFakeAuthData
    when(authConnector.currentAuthority(any(), any())).thenReturn(ad)

    when(taiService.personDetails(any())(any())).thenReturn(Future.successful(TaiRoot("", 1, "", "", None, "", "", false, None)))
  }

}
