package com.angelvb.kafka_listener.client;

import com.angelvb.kafka_listener.model.DemoRequestBody;
import com.angelvb.kafka_listener.model.NotificationServiceResponse;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

@HttpExchange(accept = MimeTypeUtils.APPLICATION_JSON_VALUE)
public interface DemoClient {

  @PostExchange("/notify")
  Mono<NotificationServiceResponse> notifyUser(@RequestBody DemoRequestBody requestBody);
}
