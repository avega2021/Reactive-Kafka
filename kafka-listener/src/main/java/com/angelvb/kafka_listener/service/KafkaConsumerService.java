package com.angelvb.kafka_listener.service;

import com.angelvb.kafka_listener.model.NotificationServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.kafka.annotation.KafkaListener;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Service
@AllArgsConstructor
public class KafkaConsumerService {

  private final NotificationService notificationService;
  private final KafkaConsumer<String, JsonNode> kafkaConsumer;
  private final ReactiveKafkaConsumerTemplate<String, JsonNode> reactiveKafkaConsumer;

  /*@KafkaListener(topics = "${spring.kafka.topics.notify}",
      groupId = "${spring.kafka.consumer.group-id}", clientIdPrefix = "kafka-demo-listener")
  public void listen(JsonNode message) {
    log.info("The message listened was: {}", message);
    getTheNotification(message, notificationService)
        .subscribe();
  }*/

  public void consumeMessages() {
    ConsumerRecords<String, JsonNode> records = kafkaConsumer.poll(Duration.ofMillis(1000));
    records.forEach(stringJsonNodeConsumerRecord -> {
      getTheNotification(stringJsonNodeConsumerRecord.value(), notificationService).subscribe();
      log.info("Consuming message: {}", stringJsonNodeConsumerRecord.value());
    });
  }

  private static Mono<NotificationServiceResponse> getTheNotification(JsonNode message, NotificationService notificationService) {
    return notificationService.notify(message)
        .doOnNext(notificationServiceResponse -> log.info("The notification response was: {}", notificationServiceResponse))
        .doOnError(throwable -> log.error("Error to send the notification"));
  }

  public void getMessages() {
    reactiveKafkaConsumer
        .receiveAutoAck()
        .onErrorResume(e -> {
          log.error("Error deserializing message: {}", e.getMessage());
          return Flux.empty();
        })
        //.take(3)
        .take(Duration.ofMillis(10000))
        .doOnNext(stringJsonNodeConsumerRecord -> {
          try {
            log.warn("Received message: {}", stringJsonNodeConsumerRecord.value());
            getTheNotification(stringJsonNodeConsumerRecord.value(), notificationService).subscribe();
          } catch (Exception e) {
            log.info("Received non-JSON string message: {}", stringJsonNodeConsumerRecord);
          }
        })
        .doOnComplete(() -> log.info("Finished consuming messages."))
        .subscribe();
  }

}
