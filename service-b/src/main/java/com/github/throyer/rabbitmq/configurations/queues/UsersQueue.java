package com.github.throyer.rabbitmq.configurations.queues;

import static org.springframework.amqp.core.BindingBuilder.bind;
import static org.springframework.amqp.core.ExchangeBuilder.topicExchange;
import static org.springframework.amqp.core.QueueBuilder.durable;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Component;

import com.github.throyer.rabbitmq.shared.DeclareQueue;
import com.github.throyer.rabbitmq.shared.QueueSettings;

@Component
public class UsersQueue implements DeclareQueue {

  @Override
  public String getAlias() {
    return "users";
  }

  @Override
  public void declare(QueueSettings settings, RabbitAdmin admin) {
    var queue = settings.getQueue();
    var retry = settings.getRetry();
    var dlq = settings.getDlq();

    TopicExchange exchange = topicExchange(queue.getExchangeName()).build();

    var usersQueue = durable(queue.getQueueName())
      .deadLetterExchange(queue.getDeadLetterExchange())
        .deadLetterRoutingKey(queue.getDeadLetterRoutingKey())
          .quorum()
            .build();

    var usersRetryQueue = durable(retry.getQueueName())
      .deadLetterExchange(retry.getDeadLetterExchange())
        .deadLetterRoutingKey(retry.getDeadLetterRoutingKey())
          .quorum()
            .ttl(settings.getRetryDelayInMilliseconds()) // 5s
              .build();

    var usersDeadLetterQueue = durable(dlq.getQueueName())
      .quorum()
        .build();

    admin.declareExchange(exchange);

    admin.declareQueue(usersQueue);
    admin.declareQueue(usersRetryQueue);
    admin.declareQueue(usersDeadLetterQueue);

    admin.declareBinding(bind(usersQueue).to(exchange).with(queue.getRoutingKey()));
    admin.declareBinding(bind(usersRetryQueue).to(exchange).with(retry.getRoutingKey()));
    admin.declareBinding(bind(usersDeadLetterQueue).to(exchange).with(dlq.getRoutingKey()));
  }
}
