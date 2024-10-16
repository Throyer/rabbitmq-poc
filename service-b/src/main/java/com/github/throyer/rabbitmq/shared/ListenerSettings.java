package com.github.throyer.rabbitmq.shared;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListenerSettings {
  private Boolean defaultRequeueRejected;
  private Integer concurrentConsumers;
  private Integer prefetch;
}
