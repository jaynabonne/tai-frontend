/*
 * Copyright 2019 HM Revenue & Customs
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

package uk.gov.hmrc.tai.model.domain

import controllers.FakeTaiPlayApplication
import org.scalatestplus.play.PlaySpec
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.{JsString, Json}
import uk.gov.hmrc.tai.model.domain.income.{Ceased, Live, PotentiallyCeased, TaxCodeIncomeSourceStatus}

class TaxCodeIncomeSpec extends PlaySpec with FakeTaiPlayApplication with I18nSupport {
  implicit val messagesApi: MessagesApi = app.injector.instanceOf[MessagesApi]

  "TaxCodeIncomeSourceStatus format" must {
    "create a valid object" when {
      "given a valid json value" in {
        JsString("Live").as[TaxCodeIncomeSourceStatus] mustBe Live
        JsString("PotentiallyCeased").as[TaxCodeIncomeSourceStatus] mustBe PotentiallyCeased
        JsString("Ceased").as[TaxCodeIncomeSourceStatus] mustBe Ceased
      }

      "throw an exception" when {
        "give an invalid json value" in {
          val exception = the[IllegalArgumentException] thrownBy JsString("Wrong").as[TaxCodeIncomeSourceStatus]
          exception.getMessage mustBe "Invalid Tax component type"
        }
      }

      "create a valid json value" when {
        "given an TaxCodeIncomeSourceStatus" in {
          Json.toJson(Live) mustBe JsString("Live")
          Json.toJson(PotentiallyCeased) mustBe JsString("PotentiallyCeased")
          Json.toJson(Ceased) mustBe JsString("Ceased")
        }
      }
    }
  }
}
