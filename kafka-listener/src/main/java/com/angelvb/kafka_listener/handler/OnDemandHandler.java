package com.angelvb.kafka_listener.handler;

import com.angelvb.kafka_listener.service.KafkaConsumerService;
import com.angelvb.kafka_listener.service.KafkaProducerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@AllArgsConstructor
public class OnDemandHandler {

  private KafkaConsumerService kafkaConsumerService;
  private KafkaProducerService kafkaProducerService;

  public Mono<ServerResponse> getMessages(ServerRequest request) {
    kafkaConsumerService.getMessages();
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("Messages consumed");
  }

  public Mono<ServerResponse> postMessages(ServerRequest request) {
    int n = Integer.parseInt(request.queryParam("n").orElse("1"));
    ObjectMapper objectMapper = new ObjectMapper();
    for (int i = 0; i < n; i++) {
      ObjectNode jsonValue = objectMapper.createObjectNode();
      jsonValue.put("description", "This is a test");
      jsonValue.put("stream", "Success");
      jsonValue.put("id", String.valueOf(i + 1));
      kafkaProducerService.sendMessage(String.valueOf(i + 1), jsonValue);
    }
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("Messages posted");
  }
}
