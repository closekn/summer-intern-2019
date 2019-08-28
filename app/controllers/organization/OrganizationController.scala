/*
 * This file is part of Nextbeat services.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

package controllers.organization

import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, MessagesControllerComponents}
import persistence.organization.dao.OrganizationDAO
import persistence.organization.model._
import persistence.organization.model.Organization._
import persistence.geo.model.Location
import persistence.geo.dao.LocationDAO
import model.site.organization._
import model.component.util.ViewValuePageLayout

// 組織
//~~~~~~~~~~~~~~~~~~~~~
class OrganizationController @javax.inject.Inject()(
  val organizationDao: OrganizationDAO,
  val daoLocation: LocationDAO,
  cc: MessagesControllerComponents
) extends AbstractController(cc) with I18nSupport {
  implicit lazy val executionContext = defaultExecutionContext

  /**
    * 組織一覧ページ
    */
  def list = Action.async { implicit request =>
    for {
      locSeq      <- daoLocation.filterByIds(Location.Region.IS_PREF_ALL)
      organizationSeq <- organizationDao.findAll
    } yield {
      val vv = SiteViewValueOrganizationList(
        layout     = ViewValuePageLayout(id = request.uri),
        location   = locSeq,
        organizations = organizationSeq
      )
      Ok(views.html.site.organization.list.Main(vv, formForOrganizationSearch))
    }
  }

  /**
   * 組織検索
   */
  def search = Action.async { implicit request =>
    formForOrganizationSearch.bindFromRequest.fold(
      errors => {
       for {
          locSeq      <- daoLocation.filterByIds(Location.Region.IS_PREF_ALL)
          organizationSeq <- organizationDao.findAll
        } yield {
          val vv = SiteViewValueOrganizationList(
            layout     = ViewValuePageLayout(id = request.uri),
            location   = locSeq,
            organizations = organizationSeq
          )
          BadRequest(views.html.site.organization.list.Main(vv, errors))
        }
      },
      form   => {
        for {
          locSeq      <- daoLocation.filterByIds(Location.Region.IS_PREF_ALL)
          organizationSeq <- form.locationIdOpt match {
            case Some(id) =>
              for {
                locations   <- daoLocation.filterByPrefId(id)
                organizationSeq <- organizationDao.filterByLocationIds(locations.map(_.id))
              } yield organizationSeq
            case None     => organizationDao.findAll
          }
        } yield {
          val vv = SiteViewValueOrganizationList(
            layout     = ViewValuePageLayout(id = request.uri),
            location   = locSeq,
            organizations = organizationSeq
          )
          Ok(views.html.site.organization.list.Main(vv, formForOrganizationSearch.fill(form)))
        }
      }
    )
  }

}