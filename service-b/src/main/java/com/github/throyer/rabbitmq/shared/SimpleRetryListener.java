package com.github.throyer.rabbitmq.shared;

import com.github.throyer.rabbitmq.utils.JSON;

import java.lang.reflect.ParameterizedType;

public interface SimpleRetryListener<T> {
  QueueSettings getSettings();
  void onMessage(Message<T> message);
  void onMaxRetryAttempts(Fail<T> fail);

  @SuppressWarnings("unchecked")
  default T parse(String message) {
    var type = (Class<T>) ((ParameterizedType) getClass().getGenericInterfaces()[0])
      .getActualTypeArguments()[0];    
    return JSON.parse(message, type);
  }
}
