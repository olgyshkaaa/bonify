package controllers

import java.io.File

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import dao.BankRepository
import models.Bank
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.mvc.MultipartFormData
import play.api.mvc.MultipartFormData.FilePart
import play.api.test.Helpers.{contentType, status, _}
import play.api.test._

import scala.concurrent.{ExecutionContext, Future}

class BanksControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with MockitoSugar {

  lazy val mockBankRepository: BankRepository = mock[BankRepository]
  implicit val sys: ActorSystem = ActorSystem("TestActor")
  implicit lazy val executionContext: ExecutionContext = inject[ExecutionContext]
  implicit val mat: ActorMaterializer = ActorMaterializer()


  "BankController GET" should {

    "return the bank name with certain id from the controller" in {
      val controller = new BankController(stubControllerComponents(), mockBankRepository, executionContext)

      when(mockBankRepository.get("123")) thenReturn Future {Some(Bank(id = "123", name ="BankTest"))}

      val response = controller.getBankNameById("123").apply(FakeRequest(GET, "/bank/123"))
      val bodyText = contentAsString(response)

      status(response) mustBe OK
      contentType(response) mustBe Some("application/json")
      bodyText.contains("name") mustBe true
      bodyText.contains("BankTest") mustBe true

    }

    "return info about not found bank from the controller" in {
      val controller = new BankController(stubControllerComponents(), mockBankRepository, executionContext)

      when(mockBankRepository.get("0000")) thenReturn Future {None}

      val response = controller.getBankNameById("0000").apply(FakeRequest(GET, "/bank/0000"))
      val bodyText = contentAsString(response)

      status(response) mustBe NOT_FOUND
      contentType(response) mustBe Some("application/json")
      bodyText.contains("message") mustBe true
      bodyText.contains("There is no bank with such id") mustBe true
    }
  }

//  "PostsController POST" should {
//
//    "return the successfully persisted post from the controller" in {
//
//      val controller = new BankController(stubControllerComponents(), mockBankRepository, executionContext)
//      val filepath = "C:\\Users\\Airat\\Desktop\\tempFile"
//      val fileName = "test.csv"
//    ///  val bufferedSource = scala.io.Source.fromFile(s"$filepath\\$fileName")
//
//      val file = new java.io.File(s"$filepath\\$fileName")
//      val part = FilePart[File](key = "fileupload", filename = "test.csv", contentType = None, ref = file)
//      val request =  FakeRequest().withBody(
//        MultipartFormData[File](dataParts = Map.empty, files = Seq(part), badParts = Nil)
//      )
//
//
//     // when(mockBankRepository.add(Bank(id = "123", name = "TestBank"))) thenReturn (Future("User successfully added"))
//
//     // val response = controller.upload().apply(FakeRequest(POST, "/bank", FakeHeaders(Seq("Content-type" -> "multipart/form-data")), bufferedSource))
//      val response = controller.upload().apply(request)
////      val bodyText = contentAsString(response)
//
//
//       status(response) mustBe OK
////      contentType(response) mustBe Some("application/json")
//   ///    bodyText.contains("File uploaded") mustBe true
//    }
//  }


//    "return info about duplicated id from the controller" in {
//      val post = """{"id":8,"title":"Title 8","body":"Body 8"}"""
//      val controller = new BankController(stubControllerComponents(), mockBankRepository, executionContext)
//      val secondResponse = controller.create().apply(FakeRequest(POST, "/api/v1/posts ", FakeHeaders(Seq("Content-type" -> "application/json")), Json.parse(post)))
//      val bodyText = contentAsString(secondResponse)
//
//      status(secondResponse) mustBe BAD_REQUEST
//      contentType(secondResponse) mustBe Some("application/json")
//      bodyText.contains("status") mustBe true
//      bodyText.contains("400") mustBe true
//      bodyText.contains("message") mustBe true
//      bodyText.contains("Id is already in use") mustBe true
//    }
//
//    "return info about the incorrect data in body from the controller" in {
//      val newPost = """{"invalidData"}"""
//      val controller = new BankController(stubControllerComponents(), mockBankRepository, executionContext)
//      val response = controller.create().apply(FakeRequest(POST, "/api/v1/posts ", FakeHeaders(Seq("Content-type" -> "application/json")), newPost))
//      val bodyText = contentAsString(response)
//
//      status(response) mustBe BAD_REQUEST
//      contentType(response) mustBe Some("text/html")
//      bodyText.contains("Bad Request") mustBe true
//      bodyText.contains("Invalid Json") mustBe true
//    }
//  }



}

