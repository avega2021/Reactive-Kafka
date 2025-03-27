package com.angelvb.kafka_listener.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class NotificationServiceResponse implements Serializable {
  @Serial
  private static final long serialVersionUID = 3620954395297098560L;
  private String status;
  private Integer code;
  private String description;
}
