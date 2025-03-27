package com.angelvb.kafka_listener.controller;

import com.angelvb.kafka_listener.service.KafkaConsumerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class KafkaController {

  private final KafkaConsumerService kafkaConsumerService;

  @GetMapping("/consume")
  public String consume() {
    kafkaConsumerService.consumeMessages();
    return "Messages consumed";
  }
}