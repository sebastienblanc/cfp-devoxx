/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Association du Paris Java User Group.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package controllers

import models._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.Future
import reactivemongo.api._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.data.Form
import models.JsonFormats._

/**
 * Devoxx France Call For Paper main application.
 * @author Nicolas Martignole
 */
object Application extends Controller with MongoController {
  def collection: JSONCollection = db.collection[JSONCollection]("bp79k8t4i6p9g53s")

  def index = Action {
    Ok("It works!")
  }

  def create = Action {

    val user = User(29, "John", "Smith", List(
      Feed("Slashdot news", "http://slashdot.org/slashdot.rdf")))
    val futureResult = collection.insert(user)
    Async {
      futureResult.map(_ => Ok)
    }
  }

  def findByName(lastName: String) = Action {
    Async {
      val cursor: Cursor[User] = collection.
        find(Json.obj("lastName" -> lastName)).
        sort(Json.obj("created" -> -1)).
        cursor[User]

      val futureUsersList: Future[List[User]] = cursor.toList
      futureUsersList.map {
        persons =>
          Ok(persons.toString())
      }
    }
  }
}