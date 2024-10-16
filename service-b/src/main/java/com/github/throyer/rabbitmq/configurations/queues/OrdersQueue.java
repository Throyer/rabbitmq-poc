package com.github.throyer.rabbitmq.configurations.queues;

import static org.springframework.amqp.core.BindingBuilder.bind;
import static org.springframework.amqp.core.ExchangeBuilder.directExchange;
import static org.springframework.amqp.core.ExchangeBuilder.topicExchange;
import static org.springframework.amqp.core.QueueBuilder.durable;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Component;

import com.github.throyer.rabbitmq.shared.DeclareQueue;
import com.github.throyer.rabbitmq.shared.QueueSettings;

@Component
public class OrdersQueue implements DeclareQueue {

  @Override
  public String getAlias() {
    return "orders";
  }

  @Override
  public void declare(QueueSettings settings, RabbitAdmin admin) {
    var queue = settings.getQueue();
    var retry = settings.getRetry();

    Exchange exchange = topicExchange(queue.getExchangeName())
      .build();

    Exchange exchangeRetry = directExchange(retry.getExchangeName())
      .build();

    var queueOrder = durable(queue.getQueueName())
      .withArgument("x-dead-letter-exchange", queue.getDeadLetterExchange())
      .build();

    var queueRetry = durable(retry.getQueueName())
      .withArgument("x-dead-letter-exchange", retry.getDeadLetterExchange())
      .withArgument("x-message-ttl", settings.getRetryDelayInMilliseconds())
      .build();

    admin.declareExchange(exchange);
    admin.declareExchange(exchangeRetry);

    admin.declareQueue(queueOrder);
    admin.declareQueue(queueRetry);
    admin.declareBinding(bind(queueOrder).to(exchange).with("").noargs());

    admin.declareBinding(bind(queueOrder).to(exchangeRetry).with("").noargs());
    admin.declareBinding(bind(queueRetry).to(exchangeRetry).with("").noargs());
  }
}
