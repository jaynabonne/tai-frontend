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

package controllers

import controllers.actions.ValidatePerson
import controllers.auth.AuthAction
import javax.inject.Inject
import play.api.mvc.{AnyContent, MessagesControllerComponents, Request, Result}
import uk.gov.hmrc.domain.Nino
import uk.gov.hmrc.play.partials.FormPartialRetriever
import uk.gov.hmrc.renderer.TemplateRenderer
import uk.gov.hmrc.tai.config.ApplicationConfig
import uk.gov.hmrc.tai.util.constants.TaiConstants

import scala.concurrent.{ExecutionContext, Future}

class ServiceController @Inject()(timeout: views.html.Timeout,
                                  manualCorrespondence: views.html.ManualCorrespondence,
                                  applicationConfig: ApplicationConfig,
                                  authenticate: AuthAction,
                                  validatePerson: ValidatePerson,
                                  mcc: MessagesControllerComponents,
                                  errorPagesHandler: ErrorPagesHandler)
                                 (implicit ec: ExecutionContext)
  extends TaiBaseController(mcc) {

  def timeoutPage() = Action.async {
    implicit request => Future.successful(Ok(timeout()))
  }

  def serviceSignout = (authenticate andThen validatePerson).async {
    implicit request =>

    if (request.taiUser.providerType == TaiConstants.AuthProviderVerify) {
        Future.successful(Redirect(applicationConfig.citizenAuthFrontendSignOutUrl).
          withSession(TaiConstants.SessionPostLogoutPage -> applicationConfig.feedbackSurveyUrl))
      } else {
        Future.successful(Redirect(applicationConfig.companyAuthFrontendSignOutUrl))
      }
  }

  def gateKeeper() = (authenticate andThen validatePerson).async {
    implicit request =>
      getGateKeeper(request.taiUser.nino)
  }

  def getGateKeeper(nino: Nino)(implicit request: Request[AnyContent]): Future[Result] = {
    Future.successful(Ok(manualCorrespondence()))
  } recoverWith errorPagesHandler.handleErrorResponse("getServiceUnavailable", nino)

}
