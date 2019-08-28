/*
 * This file is part of the Nextbeat services.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

package persistence.organization.model

import play.api.data._
import play.api.data.Forms._
import java.time.LocalDateTime

import persistence.geo.model.Location

// 組織情報
//~~~~~~~~~~~~~
case class Organization (
  id:               Option[Organization.Id],            // 組織ID
  locationId:       Location.Id,                        // 地域ID
  kanziName:        String,                             // 名前(漢字)
  furiganaName:     String,                             // 名前(ふりがな)
  englishName:      String,                             // 名前(英語名)
  address:          String,                             // 住所(詳細)
  description:      String,                             // 備考
  updatedAt:        LocalDateTime = LocalDateTime.now,  // データ更新日
  createdAt:        LocalDateTime = LocalDateTime.now   // データ作成日
)

// 組織検索
case class OrganizationSearch(
  locationIdOpt: Option[Location.Id]
)

// 組織編集
case class OrganizationEdit (
  locationId:       Option[Location.Id],
  kanziName:        Option[String],
  furiganaName:     Option[String],
  englishName:      Option[String],
  address:          Option[String],
  description:      Option[String]
)

// コンパニオンオブジェクト
object Organization {

  //  --[ 管理ID ]---------------------------------------------------------------
  type Id = Long

  //  --[ フォームの定義 ]---------------------------------------------------------------
  val formForOrganization = Form (
    mapping(
      "locationId" -> optional(text),
      "kanziName" -> optional(text),
      "furiganaName" -> optional(text),
      "englishName" -> optional(text),
      "address" -> optional(text),
      "description" -> optional(text)
    )(OrganizationEdit.apply)(OrganizationEdit.unapply)
  )

  val formForOrganizationSearch = Form(
    mapping(
      "locationId" -> optional(text),
    )(OrganizationSearch.apply)(OrganizationSearch.unapply)
  )

}