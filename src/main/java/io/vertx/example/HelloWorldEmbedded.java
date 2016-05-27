package io.vertx.example;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class HelloWorldEmbedded {

  public static void main(String[] args) {
    // Create an HTTP server which simply returns "Hello World!" to each request.
    Router router = Router.router(Vertx.vertx());
    router.route("/index").blockingHandler(routingContext -> {
        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "text/plain");

        response.end("yes");
    },false);
    Vertx.vertx().createHttpServer().requestHandler(router::accept).listen(8080);
  }

}
