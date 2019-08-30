package persistence.relation.model

import java.time.LocalDateTime

import persistence.facility.model.Facility
import persistence.organization.model.Organization

case class OrganizationFacilities (
  id:              Option[Relation.Id],
  organizationId:  Organization.Id,                // 組織id
  facilityId:      Facility.Id,                    // 施設id
  updatedAt:   LocalDateTime = LocalDateTime.now,  // データ更新日
  createdAt:   LocalDateTime = LocalDateTime.now   // データ作成日
)


// コンパニオンオブジェクト
object Relation {

  //  --[ 管理ID ]---------------------------------------------------------------
  type Id = Long

}