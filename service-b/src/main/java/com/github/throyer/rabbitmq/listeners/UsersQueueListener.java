package com.github.throyer.rabbitmq.listeners;

import static com.github.throyer.rabbitmq.utils.AmqpUtils.RABBITMQ_DEATH_HEADER_NAME;
import static com.github.throyer.rabbitmq.utils.AmqpUtils.hasExceededRetryLimit;
import static org.springframework.amqp.support.AmqpHeaders.DELIVERY_TAG;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.github.throyer.rabbitmq.shared.AmqpSettings;
import com.github.throyer.rabbitmq.shared.Listener;
import com.github.throyer.rabbitmq.utils.JSON;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.github.throyer.rabbitmq.models.User;
import com.github.throyer.rabbitmq.services.CreateUserService;
import com.rabbitmq.client.Channel;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class UsersQueueListener extends Listener<User> {

  @Autowired
  private CreateUserService createUserService;

  @Autowired
  @Qualifier("rabbitmq-template")
	private AmqpTemplate rabbitmq;

  @RabbitListener(queues = { "users-queue" }, containerFactory = "rabbitmq-container")
  public void users(
    Channel channel,
    @Payload Message content,
    @Header(name = DELIVERY_TAG) long tag,
    @Header(name = RABBITMQ_DEATH_HEADER_NAME, required = false) Map<String, ?> deaths
  ) throws IOException {    
    receive(
      true,
      5,
      channel,
      tag,
      deaths,      
      content,
      (json) -> JSON.parse(json, User.class),
      (message) -> createUserService.create(message.getBody()),
      (dlq) -> rabbitmq.convertAndSend("users-exchange", "dead-letter", dlq)
    );
  }
}
