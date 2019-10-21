package controllers

import akka.stream.Materializer
import dao.BankRepository
import models.Bank
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.mvc.ControllerComponents
import play.api.test.Helpers.{contentType, status, _}
import play.api.test._

import scala.concurrent.{ExecutionContext, Future}

class BanksControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with MockitoSugar {

  lazy val mockBankRepository: BankRepository = mock[BankRepository]
  implicit lazy val executionContext: ExecutionContext = inject[ExecutionContext]
  implicit lazy val materializer: Materializer = app.materializer
  implicit lazy val components: ControllerComponents = inject[ControllerComponents]


  "BankController GET" should {

    "return the bank name with certain id from the controller" in {
      val controller = new BankController(components, mockBankRepository, executionContext)

      when(mockBankRepository.get("123")) thenReturn Future {
        Some(Bank(id = "123", name = "BankTest"))
      }

      val response = controller.getBankNameById("123").apply(FakeRequest(GET, "/bank/123"))
      val bodyText = contentAsString(response)

      status(response) mustBe OK
      contentType(response) mustBe Some("application/json")
      bodyText.contains("name") mustBe true
      bodyText.contains("BankTest") mustBe true

    }

    "return info about not found bank from the controller" in {
      val controller = new BankController(components, mockBankRepository, executionContext)

      when(mockBankRepository.get("0000")) thenReturn Future {
        None
      }

      val response = controller.getBankNameById("0000").apply(FakeRequest(GET, "/bank/0000"))
      val bodyText = contentAsString(response)

      status(response) mustBe NOT_FOUND
      contentType(response) mustBe Some("application/json")
      bodyText.contains("message") mustBe true
      bodyText.contains("There is no bank with such id") mustBe true
    }
  }


  "PostsController POST" should {

    //    "return the successfully persisted post from the controller" in {
    //
    //      val controller = new BankController(components, mockBankRepository, executionContext)
    //      val filepath = new java.io.File(".").getCanonicalPath + "\\test\\resources"
    //      val fileName = "test.csv"
    //
    //      val file = Paths.get(s"$filepath\\$fileName")
    //      val tempFile: Files.TemporaryFile = SingletonTemporaryFileCreator.create(file)
    //      val part: FilePart[Files.TemporaryFile] = FilePart[Files.TemporaryFile](key = "fileupload", filename = "test.csv", contentType = None, ref = tempFile)
    //      val request = FakeRequest(POST, "/bank")
    //        .withHeaders("Content-Type" -> "multipart/form-data; boundary=--------------------------405566821999052788849634",
    //        "Content-Length" -> "323")
    //        .withMultipartFormDataBody(
    //          MultipartFormData[Files.TemporaryFile](dataParts = Map.empty, files = Seq(part), badParts = Nil)
    //        )
    //
    //      when(mockBankRepository.add(Bank(id = "123", name = "TestBank"))) thenReturn (Future("User successfully added"))
    //
    //      val response = controller.upload().apply(request)
    //      val bodyText = contentAsString(response)
    //
    //      status(response) mustBe OK
    //      contentType(response) mustBe Some("application/json")
    //      bodyText.contains("File uploaded") mustBe true
    //    }
    //  }


//    "return info about the incorrect data in body from the controller" in {
//      val controller = new BankController(components, mockBankRepository, executionContext)
//      val request = FakeRequest(POST, "/bank")
//        .withHeaders("Content-type" -> "multipart/form-data")
//
//      val response = controller.upload().apply(request)
//      val bodyText = contentAsString(response)
//
//      status(response) mustBe BAD_REQUEST
//      contentType(response) mustBe Some("text/html")
//      bodyText.contains("Bad Request") mustBe true
//    }


  }
}

