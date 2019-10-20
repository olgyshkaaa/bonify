package controllers

import java.io.File

import dao.BankRepository
import javax.inject.Inject
import models.Bank
import play.api.libs.Files
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext

class BankController @Inject()(
                                cc: ControllerComponents,
                                bankRepository: BankRepository,
                                implicit val executionContext: ExecutionContext
                              ) extends AbstractController(cc) {


  val filepath = "C:\\Users\\Airat\\Desktop\\tempFile"

  private def uploadFile(fileName: String): Unit = {

    val bufferedSource = scala.io.Source.fromFile(s"$filepath\\$fileName")
    for (line <- bufferedSource.getLines.drop(1)) {
      val cols = line.split(",").map(_.trim)
      bankRepository.add(Bank(id = cols(1), name = cols(0)))
    }
    bufferedSource.close
  }


  def upload: Action[MultipartFormData[Files.TemporaryFile]] = Action(parse.multipartFormData) { request =>
    println("test")
    request.body.file("fileupload").map { file =>
      val filename = file.filename
      val contentType = file.contentType
      println(contentType)

      file.ref.moveTo(new File(s"$filepath\\$filename"), replace = true)
      uploadFile(filename)
      Ok("File uploaded")
    }.getOrElse {
      Redirect("/").
        flashing("errormessage" -> "File Missing")
    }
  }


  def getBankNameById(id: String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    bankRepository.get(id).map {
      case Some(bank) => Ok(Json.toJson("name" -> bank.name))
      case None => NotFound(Json.obj("message" -> "There is no bank with such id"))
    }
  }


}
