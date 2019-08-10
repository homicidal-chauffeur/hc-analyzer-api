package simulations.controllers

import java.util.UUID

import javax.inject.Inject
import play.api.libs.json.{JsError, Json}
import play.api.mvc.{AbstractController, ControllerComponents}
import simulations.dao.{SimTags, SimulationDAO, SimulationFilter}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class SimulationsController @Inject()(cc: ControllerComponents, dao: SimulationDAO)
                                     (implicit exec: ExecutionContext) extends AbstractController(cc) {

  def search(page: Int, pageSize: Int) = Action.async(parse.json) {
    implicit request =>
      val ballotFilter = request.body.validate[SimulationFilter]

      ballotFilter.fold(
        errors => Future(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))),
        filter => dao.getItems(filter, page, pageSize).map (p => Ok(p.toJson()).as(JSON) )
      )
  }

  def edit(id: Long) = Action.async {
    implicit request =>
      dao.item(id).map(json => Ok(json).as(JSON) )
  }

  def updateTags(id: Long) = Action.async(parse.json) {
    implicit request =>
      val simTags = request.body.validate[SimTags]

      simTags.fold(
        errors => Future(BadRequest(Json.obj("status" -> "KO", "msg" -> "Validation errors", "message" -> JsError.toJson(errors)))),
        tags => {
          dao.updateTags(tags).map {
            case Success(res) => Ok(Json.obj("msg" -> "Tags updated successfully"))
            case Failure(error) => UnprocessableEntity(Json.obj("status" -> "KO", "msg" -> error.getMessage))
          }
        }
      )
  }

}
