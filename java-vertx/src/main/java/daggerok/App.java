package daggerok;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.collection.HashMap;
import io.vavr.control.Try;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class App {

  private static final ObjectMapper mapper = new ObjectMapper();

  private static Handler<HttpServerRequest> fallbackHandler = event -> {

    log.info("start async processing");

    final Map<String, String> o = HashMap.of("hello", "world",
                                             "ololo", "trololo")
                                         .toJavaMap();

    final String json = Try.of(() -> mapper.writeValueAsString(o))
                           .getOrElseGet(throwable -> "{}");
    event.response()
         .putHeader("Content-Type", "application/json")
         .end(json);
  };

  public static void main(String[] args) {

    Vertx.vertx()
         .createHttpServer()
         .requestHandler(fallbackHandler)
         .listen(8080);
  }
}
