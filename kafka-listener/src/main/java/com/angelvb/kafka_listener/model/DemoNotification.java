package com.angelvb.kafka_listener.model;

import java.io.Serial;
import java.io.Serializable;

public record DemoNotification(
    String id,
    String description,
    PromotionType promotionType) implements Serializable {
  @Serial
  private static final long serialVersionUID = 8735752142313797188L;
}

