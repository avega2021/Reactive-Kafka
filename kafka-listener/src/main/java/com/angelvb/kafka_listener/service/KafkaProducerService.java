package com.angelvb.kafka_listener.service;

import com.angelvb.kafka_listener.property.SpringKafkaProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class KafkaProducerService {

  private SpringKafkaProperty springKafkaProperty;
  private ReactiveKafkaProducerTemplate<String, JsonNode> reactiveKafkaProducerTemplate;

  public void sendMessage(String key, JsonNode message) {
    log.info("The message -> {} was send to the {} with the key: {}", message, springKafkaProperty.getTopics().getNotify(), key);
    reactiveKafkaProducerTemplate.send(springKafkaProperty.getTopics().getNotify(), key, message)
        .doOnSuccess(senderResult -> log.info("sent {} offset : {}", message, senderResult.recordMetadata().offset()))
        .subscribe();
  }
}