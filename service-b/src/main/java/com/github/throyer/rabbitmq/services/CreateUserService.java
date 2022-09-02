package com.github.throyer.rabbitmq.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.throyer.rabbitmq.models.User;
import com.github.throyer.rabbitmq.repositories.UsersRepository;

@Service
public class CreateUserService {
  @Autowired
  private UsersRepository repository;

  public User create(User user) {
    return repository.save(user);
  }
}
