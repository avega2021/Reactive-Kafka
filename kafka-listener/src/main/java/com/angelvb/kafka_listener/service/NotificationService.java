package com.angelvb.kafka_listener.service;

import com.angelvb.kafka_listener.client.DemoClient;
import com.angelvb.kafka_listener.model.DemoNotification;
import com.angelvb.kafka_listener.model.DemoRequestBody;
import com.angelvb.kafka_listener.model.NotificationServiceResponse;
import com.angelvb.kafka_listener.model.PromotionType;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Service
@AllArgsConstructor
public class NotificationService {

  private DemoClient demoClient;
  private final KafkaProducerService kafkaProducerService;

  public Mono<NotificationServiceResponse> notify(JsonNode message) {
    var demoNotification = new DemoNotification(
        message.path("id").asText(),
        message.path("description").asText(),
        PromotionType.NEW
    );
    var requestBody = new DemoRequestBody(
        message.path("stream").asText(),
        demoNotification
    );
    log.info("Attempting to notify user with message: {}", message);
    return demoClient.notifyUser(requestBody)
        .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5)))
        .doOnSuccess(response -> log.info("Successfully notified user: {}", response))
        .doOnError(throwable -> log.error("Failed to notify user: {}", throwable.getMessage()))
        .onErrorResume(throwable -> {
          RuntimeException responseException = (RuntimeException) throwable;
          log.error("Error to send notification: {}", throwable.getMessage());
          recover(message);
          return Mono.just(NotificationServiceResponse.create("Failed", 500, responseException.getMessage()));
        });
  }

  public void recover(JsonNode message) {
    log.info("The message to resend is: {}", message);
    kafkaProducerService.sendMessage(message.path("id").asText(), message);
  }
}
