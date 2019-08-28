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

  /**
   * 組織情報
   */
  def show(organizationId: Long) = Action.async { implicit request =>
    for {
      locSeq      <- daoLocation.filterByIds(Location.Region.IS_PREF_ALL)
      organization    <- organizationDao.get(organizationId)
    } yield {
      val vv = SiteViewValueOrganization(
        layout     = ViewValuePageLayout(id = request.uri),
        location   = locSeq,
        organization   = organization
      )

      Ok(views.html.site.organization.show.Main(vv, organizationId))
    }
  }

  /**
   * 組織編集
   */
  // 編集ページ
  def edit(organizationId: Long) = Action.async { implicit request =>
    for {
      locSeq      <- daoLocation.filterByIds(Location.Region.IS_PREF_ALL)
      organization    <- organizationDao.get(organizationId)
    } yield {
      val vv = SiteViewValueOrganization(
        layout     = ViewValuePageLayout(id = request.uri),
        location   = locSeq,
        organization   = organization
      )

      Ok(views.html.site.organization.edit.Main(vv, organizationId ,
        formForOrganization.fill(
          OrganizationEdit(
            Option(organization.get.locationId),
            Option(organization.get.kanziName),
            Option(organization.get.furiganaName),
            Option(organization.get.englishName),
            Option(organization.get.address),
            Option(organization.get.description)
          )
        )
      ))
    }
  }
  // 更新
  def update(organizationId: Long) = Action { implicit request =>
    val formValues = formForOrganization.bindFromRequest.get
    organizationDao.update(organizationId, formValues)
    Redirect(routes.OrganizationController.search)
  }

  /**
   * 組織削除
   */
  // 確認ページ
  def deleteCheck(organizationId: Long) = Action.async { implicit request =>
    for {
      locSeq      <- daoLocation.filterByIds(Location.Region.IS_PREF_ALL)
      organization    <- organizationDao.get(organizationId)
    } yield {
      val vv = SiteViewValueOrganization(
        layout     = ViewValuePageLayout(id = request.uri),
        location   = locSeq,
        organization   = organization
      )

      Ok(views.html.site.organization.delete.Main(vv, organizationId))
    }
  }
  // 削除
  def delete(organizationId: Long) = Action { implicit request =>
    organizationDao.delete(organizationId)
    Redirect(routes.OrganizationController.search)
  }

  /**
   * 組織追加
   */
  // 情報入力ページ
  def insertInfo = Action.async { implicit request =>
    for {
      locSeq <- daoLocation.filterByIds(Location.Region.IS_PREF_ALL)
      organization <- scala.concurrent.Future(None)
    } yield {
      val vv = SiteViewValueOrganization(
        layout = ViewValuePageLayout(id = request.uri),
        location = locSeq,
        organization = organization,
      )
      Ok(views.html.site.organization.insert.Main(vv, formForOrganization))
    }
  }
  // 追加
  def insert() = Action { implicit request =>
    formForOrganization.bindFromRequest.fold(
      errors => {
        for {
          locSeq <- daoLocation.filterByIds(Location.Region.IS_PREF_ALL)
        } yield {
          Redirect(routes.OrganizationController.search)
        }
      },
      organization   => {
        organizationDao.insert(organization)
      }
    )
    Redirect(routes.OrganizationController.search)
  }

}