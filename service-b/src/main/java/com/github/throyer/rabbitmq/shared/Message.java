package com.github.throyer.rabbitmq.shared;

import lombok.Getter;

public class Message<T> {  

  @Getter
  private final T body;
  
  private final RetryManager manager;

  public Message(T body, RetryManager manager) {
    this.body = body;
    this.manager = manager;  
  }
  
  public long getDeathCount() {
    return manager.getDeathCount();
  }

  public Boolean alreadyReachedMaxOfAttempts() {
    return manager.alreadyReachedMaxOfAttempts();
  }
}
