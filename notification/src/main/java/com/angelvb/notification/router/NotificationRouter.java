package com.angelvb.notification.router;

import com.angelvb.notification.handler.NotificationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class NotificationRouter {

  private static final String API_PATH = "/api/notify";

  @Bean
  public RouterFunction<ServerResponse> routes(NotificationHandler notificationHandler) {
    return route(POST(API_PATH), notificationHandler::notify);
  }
}
