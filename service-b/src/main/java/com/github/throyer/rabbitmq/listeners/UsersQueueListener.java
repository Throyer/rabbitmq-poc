package com.github.throyer.rabbitmq.listeners;

import com.github.throyer.rabbitmq.dtos.UserDto;
import com.github.throyer.rabbitmq.services.CreateUserService;
import com.github.throyer.rabbitmq.shared.*;
import com.github.throyer.rabbitmq.utils.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UsersQueueListener implements SimpleRetryListener<UserDto> {

  public UsersQueueListener(
    RabbitMQProperties settings,
    CreateUserService service,
    @Qualifier("rabbitmq-template") AmqpTemplate rabbitmq
  ) {
    this.settings = settings;
    this.service = service;
    this.rabbitmq = rabbitmq;
  }

  private final RabbitMQProperties settings;
  private final CreateUserService service;
	private final AmqpTemplate rabbitmq;

  @Override
  public QueueSettings getSettings() {
    return settings.get("users");
  }
  
  @Override
  public void onMessage(Message<UserDto> message) {
    service.create(message.getBody());
  }

  @Override
  public void onMaxRetryAttempts(Fail<UserDto> fail) {
    log.error("falha ao consumir mensagem, enviando para dlq. {}", JSON.stringify(fail));
    rabbitmq.convertAndSend("users-exchange", "dead-letter", fail);
  }
}
