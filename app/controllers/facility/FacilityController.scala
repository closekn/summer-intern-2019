/*
 * This file is part of Nextbeat services.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

package controllers.facility

import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, MessagesControllerComponents}
import persistence.facility.dao.FacilityDAO
import persistence.facility.model._
import persistence.facility.model.Facility._
import persistence.geo.model.Location
import persistence.geo.dao.LocationDAO
import model.site.facility._
import model.component.util.ViewValuePageLayout
import mvc.action.AuthenticationAction


// 施設
//~~~~~~~~~~~~~~~~~~~~~
class FacilityController @javax.inject.Inject()(
  val facilityDao: FacilityDAO,
  val daoLocation: LocationDAO,
  cc: MessagesControllerComponents
) extends AbstractController(cc) with I18nSupport {
  implicit lazy val executionContext = defaultExecutionContext
  val pageSize = 10

  /**
    * 施設一覧ページ
    */
  /*def list = Action.async { implicit request =>
    for {
      locSeq      <- daoLocation.filterByIds(Location.Region.IS_PREF_ALL)
      facilitySeq <- facilityDao.findAll
    } yield {
      val vv = SiteViewValueFacilityList(
        layout     = ViewValuePageLayout(id = request.uri),
        location   = locSeq,
        facilities = facilitySeq
      )
      Ok(views.html.site.facility.list.Main(vv, formForFacilitySearch))
    }
  }*/

  /**
   * 施設検索
   */
  def search(page: Int) = Action.async { implicit request =>
    formForFacilitySearch.bindFromRequest.fold(
      errors => {
       for {
          locSeq      <- daoLocation.filterByIds(Location.Region.IS_PREF_ALL)
          facilitySeq <- facilityDao.findAll
        } yield {
          val vv = SiteViewValueFacilityList(
            layout     = ViewValuePageLayout(id = request.uri),
            location   = locSeq,
            facilities = facilitySeq
          )
          BadRequest(views.html.site.facility.list.Main(vv, errors, page, pageSize))
        }
      },
      form   => {
        for {
          locSeq      <- daoLocation.filterByIds(Location.Region.IS_PREF_ALL)
          facilitySeq <- form.locationIdOpt match {
            case Some(id) =>
              for {
                locations   <- daoLocation.filterByPrefId(id)
                facilitySeq <- facilityDao.filterByLocationIds(locations.map(_.id))
              } yield facilitySeq
            case None     => facilityDao.findAll
          }
        } yield {
          val vv = SiteViewValueFacilityList(
            layout     = ViewValuePageLayout(id = request.uri),
            location   = locSeq,
            facilities = facilitySeq
          )
          Ok(views.html.site.facility.list.Main(vv, formForFacilitySearch.fill(form), page, pageSize))
        }
      }
    )
  }

  /**
   * 施設情報
   */
  def show(facilityId: Long) = Action.async { implicit request =>
    for {
      locSeq      <- daoLocation.filterByIds(Location.Region.IS_PREF_ALL)
      facility    <- facilityDao.get(facilityId)
    } yield {
      val vv = SiteViewValueFacility(
        layout     = ViewValuePageLayout(id = request.uri),
        location   = locSeq,
        facility   = facility
      )

      Ok(views.html.site.facility.show.Main(vv, facilityId))
    }
  }

  /**
   * 施設編集
   */
  // 編集ページ
  def edit(facilityId: Long) = Action.async { implicit request =>
    for {
      locSeq      <- daoLocation.filterByIds(Location.Region.IS_PREF_ALL)
      facility    <- facilityDao.get(facilityId)
    } yield {
      val vv = SiteViewValueFacility(
        layout     = ViewValuePageLayout(id = request.uri),
        location   = locSeq,
        facility   = facility
      )

      Ok(views.html.site.facility.edit.Main(vv, facilityId ,
        formForFacility.fill(
          FacilityEdit(
            Option(facility.get.locationId),
            Option(facility.get.name),
            Option(facility.get.address),
            Option(facility.get.description)
          )
        )
      ))
    }
  }
  // 更新
  def update(facilityId: Long) = Action { implicit request =>
    val formValues = formForFacility.bindFromRequest.get
    facilityDao.update(facilityId, formValues)
    Redirect(routes.FacilityController.search(1))
  }

  /**
   * 施設削除
   */
  // 確認ページ
  def deleteCheck(facilityId: Long) = Action.async { implicit request =>
    for {
      locSeq      <- daoLocation.filterByIds(Location.Region.IS_PREF_ALL)
      facility    <- facilityDao.get(facilityId)
    } yield {
      val vv = SiteViewValueFacility(
        layout     = ViewValuePageLayout(id = request.uri),
        location   = locSeq,
        facility   = facility
      )

      Ok(views.html.site.facility.delete.Main(vv, facilityId))
    }
  }
  // 削除
  def delete(facilityId: Long) = Action { implicit request =>
    facilityDao.delete(facilityId)
    Redirect(routes.FacilityController.search(1))
  }

  /**
   * 施設追加
   */
  // 情報入力ページ
  def insertInfo = Action.async { implicit request =>
    for {
      locSeq <- daoLocation.filterByIds(Location.Region.IS_PREF_ALL)
      facility <- scala.concurrent.Future(None)
    } yield {
      val vv = SiteViewValueFacility(
        layout = ViewValuePageLayout(id = request.uri),
        location = locSeq,
        facility = facility,
      )
      Ok(views.html.site.facility.insert.Main(vv, formForFacility))
    }
  }
  // 追加
  def insert() = Action { implicit request =>
    formForFacility.bindFromRequest.fold(
      errors => {
        for {
          locSeq <- daoLocation.filterByIds(Location.Region.IS_PREF_ALL)
        } yield {
          Redirect(routes.FacilityController.search(1))
        }
      },
      facility   => {
        facilityDao.insert(facility)
      }
    )
    Redirect(routes.FacilityController.search(1))
  }

}
