package com.github.throyer.rabbitmq.listeners;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.throyer.rabbitmq.models.User;
import com.github.throyer.rabbitmq.services.CreateUserService;
import com.github.throyer.rabbitmq.utils.JSON;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class UsersQueueListener {

  @Autowired
  private CreateUserService createUserService;

  @RabbitListener(queues =  "users", containerFactory = "rabbitmq-container")
  public void users(User user) {
    createUserService.create(user);
    log.info("usuário criado com sucesso \n{}", JSON.stringify(user));
  }
}
