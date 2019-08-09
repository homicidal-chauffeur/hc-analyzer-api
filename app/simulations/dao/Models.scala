package simulations.dao

import java.time.LocalDate
import play.api.libs.functional.syntax._
import play.api.libs.json.{Reads, __}

case class SimulationFilter(dateFrom: Option[LocalDate], dateTo: Option[LocalDate], tags: Option[String])

object SimulationFilter {
  implicit val reads: Reads[SimulationFilter] = (
      (__ \ "dateFrom").readNullable[LocalDate] and
      (__ \ "dateTo").readNullable[LocalDate] and
        (__ \ "tags").readNullable[String]
    )(SimulationFilter.apply _)
}
