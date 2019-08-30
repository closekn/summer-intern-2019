/*
 * This file is part of the MARIAGE services.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

package model.site.organization

import model.component.util.ViewValuePageLayout
import persistence.geo.model.Location
import persistence.relation.model.Relation
import persistence.organization.model.Organization

// 表示: 組織一覧
case class SiteViewValueOrganizationList
(
  layout:      ViewValuePageLayout,
  location:    Seq[Location],
  organizations: Seq[Organization],
  relations: Seq[Relation],
)
