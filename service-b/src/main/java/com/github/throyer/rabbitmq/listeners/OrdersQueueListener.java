package com.github.throyer.rabbitmq.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.throyer.rabbitmq.models.Order;
import com.github.throyer.rabbitmq.shared.Fail;
import com.github.throyer.rabbitmq.shared.Message;
import com.github.throyer.rabbitmq.shared.QueueSettings;
import com.github.throyer.rabbitmq.shared.RabbitMQProperties;
import com.github.throyer.rabbitmq.shared.SimpleRetryListener;
import com.github.throyer.rabbitmq.utils.JSON;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrdersQueueListener implements SimpleRetryListener<Order> {

  @Autowired
  private RabbitMQProperties settings;

  @Override
  public QueueSettings getSettings() {
    return settings.get("orders");
  }

  @Override
  public Order parse(String message) {
    return JSON.parse(message, Order.class);
  }

  @Override
  public void onMessage(Message<Order> message) {
    log.info("receive order: {}", JSON.stringify(message));
  }

  @Override
  public void onMaxRetryAttempts(Fail<Order> fail) {
    log.error("não foi possivel processar order.");
  }
}
