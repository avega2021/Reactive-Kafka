package com.angelvb.kafka_listener.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

@Slf4j
public class SafeJsonDeserializer<T> implements Deserializer<T> {

  private final Class<T> clazz;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public SafeJsonDeserializer(Class<T> clazz) {
    this.clazz = clazz;
  }

  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {
  }

  @Override
  public T deserialize(String topic, byte[] data) {
    if (data == null) {
      return null;
    }

    try {
      return objectMapper.readValue(data, clazz);
    } catch (Exception e) {
      log.error("Error deserializing message: {}", e.getMessage());
      return null;
    }
  }

  @Override
  public void close() {
  }
}
