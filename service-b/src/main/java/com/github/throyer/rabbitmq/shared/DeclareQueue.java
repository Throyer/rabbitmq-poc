package com.github.throyer.rabbitmq.shared;

import org.springframework.amqp.rabbit.core.RabbitAdmin;

public interface DeclareQueue {
  String getAlias();
  void declare(QueueSettings settings, RabbitAdmin admin);
}
