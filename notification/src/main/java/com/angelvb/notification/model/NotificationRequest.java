package com.angelvb.notification.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class NotificationRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = 3749050074168583641L;

  private String streamType;
  private DemoNotification message;

}
