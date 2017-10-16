package daggerok;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

import java.net.Inet4Address;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
public class App extends AbstractVerticle {

  final static int port = 8080;
  final static Vertx vertx = Vertx.vertx();
  final static Router router = Router.router(vertx);

  @Override
  public void start(Future<Void> future) {
/*
    router.route("/static/*")
          .handler(request -> StaticHandler.create("static"));
*/
    router.route("/")
          .handler(request -> request.response()
                                     .putHeader("content-type", "text/html")
                                     .sendFile("static/index.html"));
    router.route("/*")
          .handler(request -> responseFrom(request).end(
              jsonOf("api", asList(
                  singletonMap("get", base() + "/"),
                  singletonMap("get", base() + "/api/v1/hello")))));

    vertx.createHttpServer()
         .requestHandler(router::accept)
         .listen(config().getInteger("http.port", port), result -> {
           if (result.succeeded()) future.complete();
           else future.fail(result.cause());
         });
  }

  /* helpers */

  @SneakyThrows
  static String base() {
    return "http://" + Inet4Address.getLocalHost().getHostAddress() + ":" + port;
  }

  static String jsonOf(final String key, final Object value) {
    return Json.encodePrettily(singletonMap(key, value));
  }

  static HttpServerResponse responseFrom(final RoutingContext request) {
    return request.response()
                  .putHeader("content-type", "application/json; charset=utf-8");
  }
}
