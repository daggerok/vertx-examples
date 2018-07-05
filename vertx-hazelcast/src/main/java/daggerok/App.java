package daggerok;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import lombok.extern.slf4j.Slf4j;

//tag::content[]
@Slf4j
public class App {

  static final String ADDR = "127.0.0.1";
  static final ObjectMapper mapper = new ObjectMapper();
  static final String PORT = System.getenv().getOrDefault("PORT", "8080");
  static final int port = Integer.valueOf(PORT);

  public static void main(String[] args) {
    final HazelcastClusterManager clusterManager = new HazelcastClusterManager();
    final VertxOptions options = new VertxOptions().setClusterManager(clusterManager);

    Vertx.clusteredVertx(options, result -> {
      if (result.failed()) {
        log.info("result failed");
        return;
      }

      final Vertx vertx = result.result();
      final EventBus eventBus = vertx.eventBus();
      final HttpServer server = vertx.createHttpServer();
      final MessageConsumer<String> consumer = eventBus.consumer(ADDR);

      consumer.handler(message -> {
        final String body = message.body();
        final JsonObject jsonObject = new JsonObject(body);
        final String jsonString = jsonObject.encode();
        log.info("msg-{} in: {}", port, jsonString);
      });

      server.requestHandler(request -> {
        request.bodyHandler(body -> {
          final JsonObject jsonObject = body.toJsonObject();
          final String jsonString = jsonObject.encode();
          log.info("http-{} in: {}", port, jsonString);
          eventBus.send(ADDR, jsonString);
          request.response()
                 .putHeader("Content-Type", "application/json")
                 .end(jsonString);
        });
      });

      server.listen(port, event -> {
        log.info("listening {} port.", port);
      });
    });
  }
}
//end::content[]
