package com.github.throyer.rabbitmq.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.throyer.rabbitmq.models.User;
import com.github.throyer.rabbitmq.repositories.UsersRepository;
import com.github.throyer.rabbitmq.utils.JSON;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CreateUserService {
  @Autowired
  private UsersRepository repository;

  public User create(User user) {
    var created = repository.save(user);
    log.info("usuário criado com sucesso \n{}", JSON.stringify(created));
    return created;
  }
}
