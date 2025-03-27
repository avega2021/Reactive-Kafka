package com.angelvb.kafka_listener.router;

import com.angelvb.kafka_listener.handler.OnDemandHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class NotificationRouter {

  private static final String API_PATH = "/api/onDemand";
  private static final String API_PATH_POST = "/api/postMessages";

  @Bean
  public RouterFunction<ServerResponse> routes(OnDemandHandler onDemandHandler) {
    return route(GET(API_PATH), onDemandHandler::getMessages)
        .andRoute(GET(API_PATH_POST),onDemandHandler::postMessages);
  }
}
