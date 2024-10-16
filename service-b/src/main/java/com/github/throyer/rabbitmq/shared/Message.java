package com.github.throyer.rabbitmq.shared;

import lombok.Getter;

public class Message<T> {  

  @Getter
  private final T body;
  
  private final ChannelManager channel;

  public Message(T body, ChannelManager manager) {
    this.body = body;
    this.channel = manager;  
  }
  
  public long getDeathCount() {
    return channel.getDeathCount();
  }

  public long getCurrentAttempt() {
    return channel.getCurrentAttempt();
  }

  public Boolean alreadyReachedMaxOfAttempts() {
    return channel.alreadyReachedMaxOfAttempts();
  }
}
