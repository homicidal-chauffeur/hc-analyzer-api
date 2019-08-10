package simulations.dao

import play.api.libs.json.JodaReads._

import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json.{Reads, __}

case class SimulationFilter(dateFrom: Option[DateTime], dateTo: Option[DateTime], tags: Option[String])

object SimulationFilter {
  implicit val reads: Reads[SimulationFilter] = (
      (__ \ "dateFrom").readNullable[DateTime] and
      (__ \ "dateTo").readNullable[DateTime] and
        (__ \ "tags").readNullable[String]
    )(SimulationFilter.apply _)
}
