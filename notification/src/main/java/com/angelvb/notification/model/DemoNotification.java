package com.angelvb.notification.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class DemoNotification implements Serializable {

  @Serial
  private static final long serialVersionUID = 7447022889643752474L;

  private String id;
  private String description;
  private PromotionType promotionType;
}
