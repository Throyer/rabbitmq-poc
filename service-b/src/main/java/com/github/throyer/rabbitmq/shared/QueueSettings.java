package com.github.throyer.rabbitmq.shared;

import static lombok.AccessLevel.NONE;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueueSettings {
  @Getter(NONE)
  private Boolean enabled;

  private String alias;

  private Long retryDelayInMilliseconds;
  private Long maxRetryAttempts;

  private Queue queue;
  private Queue retry;
  private Queue dlq;

  public Boolean isEnabled() {
    return this.enabled;
  }
  
  @Getter
  @Setter
  public static class Queue {
    private String routingKey;
    private String exchangeName;
    private String queueName;
  }
}
