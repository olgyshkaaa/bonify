package models

import play.api.libs.json.{Format, Json}

case class Bank(id: String, name: String)

object Bank {

  implicit val bankFormat: Format[Bank] = Json.format[Bank]

}