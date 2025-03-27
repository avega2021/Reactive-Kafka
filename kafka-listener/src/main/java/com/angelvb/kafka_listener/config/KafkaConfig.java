package com.angelvb.kafka_listener.config;

import com.angelvb.kafka_listener.property.SpringKafkaProperty;
import com.angelvb.kafka_listener.util.SafeJsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.support.converter.ByteArrayJsonMessageConverter;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.SenderOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

  private final ObjectMapper objectMapper;

  @Bean
  JsonMessageConverter jsonMessageConverter() {
    return new ByteArrayJsonMessageConverter(objectMapper);
  }

  @Bean
  DefaultKafkaProducerFactoryCustomizer defaultKafkaProducerFactoryCustomizer() {
    return pf -> pf.setValueSerializer(new JsonSerializer<>(objectMapper));
  }

  @Bean
  public ReactiveKafkaProducerTemplate<String, JsonNode> reactiveKafkaProducer(SpringKafkaProperty springKafkaProperty) {

    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, springKafkaProperty.getBootstrapServers());
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

    return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(props));
  }

  @Bean
  public KafkaConsumer<String, JsonNode> kafkaConsumer(SpringKafkaProperty springKafkaProperty) {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, springKafkaProperty.getBootstrapServers());
    props.put(ConsumerConfig.GROUP_ID_CONFIG, springKafkaProperty.getConsumer().getGroupId());
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    KafkaConsumer<String, JsonNode> kafkaConsumer = new KafkaConsumer<>(props, new StringDeserializer(), new JsonDeserializer<>(JsonNode.class));
    kafkaConsumer.subscribe(Collections.singletonList(springKafkaProperty.getTopics().getNotify()));
    return kafkaConsumer;
  }

  @Bean
  public ReceiverOptions<String, JsonNode> kafkaReceiver(SpringKafkaProperty springKafkaProperty) {
    Map<String, Object> config = new HashMap<>();
    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, springKafkaProperty.getBootstrapServers());
    config.put(ConsumerConfig.GROUP_ID_CONFIG, springKafkaProperty.getConsumer().getGroupId() + "-reactive");
    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SafeJsonDeserializer.class);
    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    ReceiverOptions<String, JsonNode> basicReceiverOptions = ReceiverOptions.create(config);
    return basicReceiverOptions
        .withKeyDeserializer(new StringDeserializer())
        .withValueDeserializer(new SafeJsonDeserializer<>(JsonNode.class))
        .subscription(Collections.singletonList(springKafkaProperty.getTopics().getNotify()));

  }

  @Bean
  public ReactiveKafkaConsumerTemplate<String, JsonNode> reactiveKafkaConsumer(ReceiverOptions<String, JsonNode> kafkaReceiverOptions) {
    return new ReactiveKafkaConsumerTemplate<>(kafkaReceiverOptions);
  }

}