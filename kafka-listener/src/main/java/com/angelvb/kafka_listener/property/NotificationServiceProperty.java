package com.angelvb.kafka_listener.property;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ConfigurationProperties("notification.service")
public class NotificationServiceProperty {
  private String url;
}
