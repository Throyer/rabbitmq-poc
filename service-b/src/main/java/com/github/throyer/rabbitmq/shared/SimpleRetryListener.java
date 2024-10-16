package com.github.throyer.rabbitmq.shared;

public interface SimpleRetryListener<T> {
  QueueSettings getSettings();
  T parse(String message);
  void onMessage(Message<T> message);
  void onMaxRetryAttempts(Fail<T> fail);
}
