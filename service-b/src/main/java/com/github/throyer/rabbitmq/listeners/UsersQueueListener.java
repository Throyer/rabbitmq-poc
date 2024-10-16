package com.github.throyer.rabbitmq.listeners;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.throyer.rabbitmq.models.User;
import com.github.throyer.rabbitmq.services.CreateUserService;
import com.github.throyer.rabbitmq.shared.Fail;
import com.github.throyer.rabbitmq.shared.Message;
import com.github.throyer.rabbitmq.shared.QueueSettings;
import com.github.throyer.rabbitmq.shared.SimpleRetryListener;
import com.github.throyer.rabbitmq.shared.UsersAmqpProperties;
import com.github.throyer.rabbitmq.utils.JSON;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UsersQueueListener implements SimpleRetryListener<User> {

  @Autowired
  private UsersAmqpProperties settings;

  @Autowired
  private CreateUserService service;

  @Autowired
  @Qualifier("rabbitmq-template")
	private AmqpTemplate rabbitmq;

  @Override
  public QueueSettings getSettings() {
    return settings.get("users");
  }

  @Override
  public User parse(String message) {
    return JSON.parse(message, User.class);
  }

  @Override
  public void onMessage(Message<User> message) {
    service.create(message.getBody());
  }

  @Override
  public void onMaxRetryAttempts(Fail<User> fail) {
    log.error("falha ao consumir mensagem, enviando para dlq.");
    rabbitmq.convertAndSend("users-exchange", "dead-letter", fail);
  }
}
