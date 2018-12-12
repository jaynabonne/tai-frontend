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

package uk.gov.hmrc.tai.viewModels.taxCodeChange


import uk.gov.hmrc.play.views.helpers.MoneyPounds
import uk.gov.hmrc.tai.util.yourTaxFreeAmount.{AllowancesAndDeductions, TaxFreeInfo}
import uk.gov.hmrc.tai.util.ViewModelHelper
import uk.gov.hmrc.tai.viewModels.TaxFreeAmountSummaryViewModel

case class YourTaxFreeAmountViewModel(previousTaxFreeInfo: TaxFreeInfo,
                                      currentTaxFreeInfo: TaxFreeInfo,
                                      taxFreeAmountSummary: TaxFreeAmountSummaryViewModel,
                                      allowancesAndDeductions: AllowancesAndDeductions
                                      ) {}

object YourTaxFreeAmountViewModel extends ViewModelHelper {
  def prettyPrint(value: BigDecimal) : String = {
    withPoundPrefixAndSign(MoneyPounds(value, 0))
  }
}
