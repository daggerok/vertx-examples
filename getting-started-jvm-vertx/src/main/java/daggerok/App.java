package daggerok;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

import java.net.Inet4Address;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
public class App {

  final static int port = 8080;
  final static Vertx vertx = Vertx.vertx();
  final static Router router = Router.router(vertx);

  public static void main(String[] args) {

    router.route("/")
          .handler(App::indexHtml);

    router.route("/api/v1/hello")
          .handler(App::hello);

    router.route("/api/v1/hello/:name")
          .handler(App::helloName);

    router.route("/*")
          .handler(App::info);

    vertx.createHttpServer()
         .requestHandler(router::accept)
         .listen(port);
  }

  /* router handlers */

  static void indexHtml(final RoutingContext routingContext) {

    routingContext.response()
                  .putHeader("content-type", "text/html")
                  .sendFile("static/index.html");
  }

  static void hello(final RoutingContext routingContext) {
    responseFrom(routingContext).end(jsonOf("message", "Hi!"));
  }

  private static void helloName(final RoutingContext routingContext) {
    responseFrom(routingContext).end(jsonOf("message", "Hello, " + routingContext.pathParam("name") + "!"));
  }

  static void info(final RoutingContext routingContext) {

    responseFrom(routingContext).end(
        jsonOf("api", asList(
            singletonMap("get", base() + "/"),
            singletonMap("get", base() + "/api/v1/hello"))));
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
