package com.github.throyer.rabbitmq.services;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.throyer.rabbitmq.models.User;

@Service
public class UsersService {
  @Autowired
	private AmqpTemplate rabbitmq;

  public User create(String name) {
    var user = new User(name);
    rabbitmq.convertSendAndReceive("users", user);
    return user;
  }
}
