/*
 * This file is part of the Nextbeat services.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

package persistence.organization.dao

import java.time.LocalDateTime
import scala.concurrent.Future

import slick.jdbc.JdbcProfile
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import persistence.organization.model._
import persistence.geo.model.Location

// DAO: 施設情報
//~~~~~~~~~~~~~~~~~~
class OrganizationDAO @javax.inject.Inject()(
  val dbConfigProvider: DatabaseConfigProvider
) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  // --[ リソース定義 ] --------------------------------------------------------
  lazy val slick = TableQuery[OrganizationTable]

  // --[ データ処理定義 ] ------------------------------------------------------
  /**
   * 組織を取得
   */
  def get(id: Organization.Id): Future[Option[Organization]] =
    db.run {
      slick
        .filter(_.id === id)
        .result.headOption
    }

  /**
   * 組織を全件取得する
   */
  def findAll: Future[Seq[Organization]] =
    db.run {
      slick.result
    }

  /**
   * 地域から組織を取得
   * 検索業件: ロケーションID
   */
  def filterByLocationIds(locationIds: Seq[Location.Id]): Future[Seq[Organization]] =
    db.run {
      slick
        .filter(_.locationId inSet locationIds)
        .result
    }

  // --[ テーブル定義 ] --------------------------------------------------------
  class OrganizationTable(tag: Tag) extends Table[Organization](tag, "organization") {


    // Table's columns
    /* @1 */ def id            = column[Organization.Id] ("id", O.PrimaryKey, O.AutoInc)
    /* @2 */ def locationId    = column[Location.Id]     ("location_id")
    /* @3 */ def kanziName     = column[String]          ("kanziName")
    /* @4 */ def furiganaName  = column[String]          ("furiganaName")
    /* @5 */ def englishName   = column[String]          ("englishName")
    /* @6 */ def address       = column[String]          ("address")
    /* @7 */ def description   = column[String]          ("description")
    /* @8 */ def updatedAt     = column[LocalDateTime]   ("updated_at")
    /* @9 */ def createdAt     = column[LocalDateTime]   ("created_at")

    // The * projection of the table
    def * = (
      id.?, locationId, kanziName, furiganaName, englishName, address, description,
      updatedAt, createdAt
    ) <> (
      /** The bidirectional mappings : Tuple(table) => Model */
      (Organization.apply _).tupled,
      /** The bidirectional mappings : Model => Tuple(table) */
      (v: TableElementType) => Organization.unapply(v).map(_.copy(
        _8 = LocalDateTime.now
      ))
    )
  }
}