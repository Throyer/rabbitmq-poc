package com.github.throyer.rabbitmq.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static lombok.AccessLevel.NONE;

@Getter
@AllArgsConstructor
public class AmqpSettings {
  @Getter(NONE)
  private Boolean enabled;

  private String exchangeName;
  private String queueName;
  private String queueNameRetry;
  private String queueNameDlq;
  private String routingKey;
  private String routingKeyRetry;
  private String routingKeyDlq;
  private String exchangeNameRetry;
  private Long retryDelayInMilliseconds;
  private Long maxRetryAttempts;
  
  public Boolean isEnabled() {
    return this.enabled;
  }
}
