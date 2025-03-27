package com.angelvb.notification.handler;

import com.angelvb.notification.model.NotificationRequest;
import com.angelvb.notification.model.NotificationResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class NotificationHandler {

  public Mono<ServerResponse> notify(ServerRequest request) {
    Mono<NotificationRequest> responseMono = request.bodyToMono(NotificationRequest.class);
    return  responseMono.flatMap(notificationRequest -> {
      if (notificationRequest.getStreamType().equalsIgnoreCase("SUCCESS")) {
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(NotificationResponse.create("Success", 200, "The notification was send successfully"));
      } else if (notificationRequest.getStreamType().equalsIgnoreCase("BAD_REQUEST")){
        return ServerResponse.badRequest()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(NotificationResponse.create("Failure", 400, "BadRequest"));
      }else {
        return ServerResponse.status(500)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(NotificationResponse.create("Failure", 500, "Error to send notification"));
      }
    });
  }
}
