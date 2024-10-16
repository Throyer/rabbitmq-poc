package com.github.throyer.rabbitmq.services;

import com.github.throyer.rabbitmq.dtos.UserDto;
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

  public User create(UserDto dto) {
    var created = repository.save(new User(dto));
    log.info("usuário criado com sucesso \n{}", JSON.stringify(created));
    return created;
  }
}
