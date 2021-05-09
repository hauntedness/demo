package scala.webclients

import io.vertx.core.Vertx
import io.vertx.core.Vertx.vertx
import io.vertx.ext.web.client.WebClient

class MyVertx() extends Serializable {
  private var vertx1: Vertx = Vertx.vertx()

  val client: WebClient = WebClient.create(vertx1)
}
