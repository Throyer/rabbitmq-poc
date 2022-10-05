package com.github.throyer.rabbitmq.services;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.github.throyer.rabbitmq.models.User;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class UsersService {
  @Autowired
  @Qualifier("rabbitmq-template")
	private AmqpTemplate rabbitmq;

  public User create(String name) {
    var user = new User(name);
    rabbitmq.convertAndSend("users-exchange", "create", user);
    log.info("usuário {} enviado com sucesso", user);
    return user;
  }
}
