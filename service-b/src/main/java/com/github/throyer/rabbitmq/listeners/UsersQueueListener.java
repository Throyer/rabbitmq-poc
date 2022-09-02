package com.github.throyer.rabbitmq.listeners;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.throyer.rabbitmq.models.User;
import com.github.throyer.rabbitmq.services.CreateUserService;

@Service
public class UsersQueueListener {

  @Autowired
  private CreateUserService createUserService;

  @RabbitListener(queues =  "users", containerFactory = "rabbitmq-container")
  public void users(User user) {
    createUserService.create(user);
  }
}
