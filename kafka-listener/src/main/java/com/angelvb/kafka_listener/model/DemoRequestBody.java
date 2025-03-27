package com.angelvb.kafka_listener.model;

import java.io.Serial;
import java.io.Serializable;

public record DemoRequestBody(
    String streamType,
    DemoNotification message) implements Serializable {
  @Serial
  private static final long serialVersionUID = -3195256102602104738L;
}
