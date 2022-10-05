package com.github.throyer.rabbitmq.listeners;

import static com.github.throyer.rabbitmq.utils.RetryUtils.hasExceededRetryLimit;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.github.throyer.rabbitmq.models.User;
import com.github.throyer.rabbitmq.services.CreateUserService;
import com.rabbitmq.client.Channel;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class UsersQueueListener {

  @Autowired
  private CreateUserService createUserService;

  @Autowired
  @Qualifier("rabbitmq-template")
	private AmqpTemplate rabbitmq;

  @RabbitListener(queues = { "users-queue" }, containerFactory = "rabbitmq-container")
  public void users(
    User user,
    @Header(required = false, name = "x-death") HashMap<String, ?> deathHeader,
    Message message,
    Channel channel
  ) throws IOException {
    var properties = message.getMessageProperties();
    var deliveryTag = properties.getDeliveryTag();
    try {
      log.info("trying process the message: [{}]", deliveryTag);
      
      if (hasExceededRetryLimit(deathHeader, 5l)) {
        log.error("max attempts exceeded to message: [{}], moving to dead-letter-queue", deliveryTag);
        rabbitmq.convertAndSend("users-exchange", "dead-letter", user);
        channel.basicAck(deliveryTag, false);
        return;
      }
      
      createUserService.create(user);

      log.info("process success to message: [{}]", deliveryTag);
      channel.basicAck(deliveryTag, false);      
    } catch (Exception exception) {

      log.warn("process message [{}] fails, moving to retry-queue", deliveryTag);
      channel.basicReject(deliveryTag, false);
    }
  }
}
