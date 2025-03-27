package com.angelvb.kafka_listener.property;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ConfigurationProperties("spring.kafka")
public class SpringKafkaProperty {

  @Data
  public static class Topics {
    private String notify;
  }

  @Data
  public static class Consumer {
    private String groupId;
  }

  private Topics topics = new Topics();
  private Consumer consumer = new Consumer();
  private String bootstrapServers;

}
