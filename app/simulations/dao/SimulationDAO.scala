package simulations.dao

import java.sql.Date

import com.google.inject.Provider
import javax.inject.Inject
import play.api.{Application, Logging}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import common.PgProfileWithSpecialTypes
import slick.jdbc.JdbcProfile
import common.utils.{ActionBuilderUtils, DateUtils, PageDbJson}
import slick.jdbc.SQLActionBuilder

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

class SimulationDAO @Inject()(appProvider: Provider[Application],
                              protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfig[PgProfileWithSpecialTypes] with Logging {

  implicit lazy val app = appProvider.get()
  val dbConfig = dbConfigProvider.get[PgProfileWithSpecialTypes]

  import profile.api._

  def getItems(filter: SimulationFilter,
               page: Int = 0, pageSize: Int = 25): Future[PageDbJson] = {
    val offset = pageSize * page

    val cond = parseFilter(filter)

    val countSql = sql"SELECT COUNT(*) FROM simulation_results s "

    val itemsSqlStart =
      sql"""
         select array_to_json(array_agg(t)) AS items from (
            SELECT *
            FROM simulation_results s
      """
    val itemsSqlEnd = sql" ORDER BY id DESC LIMIT $pageSize OFFSET $offset) t;"


    val action: DBIO[PageDbJson] = for {
      count <- ActionBuilderUtils.concat(countSql, cond).as[Int].head
      items <- ActionBuilderUtils.concat(ActionBuilderUtils.concat(itemsSqlStart, cond), itemsSqlEnd).as[String].head
    } yield PageDbJson(items, count, offset + pageSize < count)

    db.run(
      action
    )
  }

  def parseFilter(filter: SimulationFilter): SQLActionBuilder = {
    var cond = sql" WHERE 1 = 1 "

    filter.tags.map(c => cond =  ActionBuilderUtils.concat(cond, sql" AND s.tags ilike ${s"%$c%"}"))
    filter.dateFrom.map(c => cond =  ActionBuilderUtils.concat(cond, sql" AND s.date_at::DATE >= ${DateUtils.sqlDate(c)}::DATE"))
    filter.dateTo.map(c => cond =  ActionBuilderUtils.concat(cond, sql" AND s.date_at::DATE <= ${DateUtils.sqlDate(c)}::DATE"))

    cond
  }

  def item(id: Long): Future[String] = {
    val sql = sql"""
         select row_to_json(t) AS item from (
           SELECT * FROM simulation_details
             WHERE id = $id
                   ) t;
      """

    val action: DBIO[String] = sql.as[String].head

    db.run(action)
  }

  def updateTags(tags: SimTags): Future[Try[Int]] = {
    val sql = sqlu""" UPDATE simulations SET tags = ${tags.tags} WHERE id = ${tags.id} """

    db.run(sql.asTry)
  }
}
