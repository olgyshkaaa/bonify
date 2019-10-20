package dao

import com.google.inject.Inject
import models.Bank
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

trait BankComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  class BankTableDef(tag: Tag) extends Table[Bank](tag, "banks") {
    def * = (id, name) <> ((Bank.apply _).tupled, Bank.unapply)

    def id: Rep[String] = column[String]("id", O.PrimaryKey)

    def name: Rep[String] = column[String]("name")

  }

}

class BankRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends BankComponent
    with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._
  val banks = TableQuery[BankTableDef]

  def add(bank: Bank): Future[String] = {
    dbConfig.db.run(banks += bank).map(res => "User successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def get(id: String): Future[Option[Bank]] = {
    dbConfig.db.run(banks.filter(_.id === id).result.headOption)
  }

}
