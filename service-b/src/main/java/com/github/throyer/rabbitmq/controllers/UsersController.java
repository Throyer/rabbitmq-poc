package com.github.throyer.rabbitmq.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.throyer.rabbitmq.models.User;
import com.github.throyer.rabbitmq.repositories.UsersRepository;

@RequestMapping("/api/v1/users")
public class UsersController {

  @Autowired
  private UsersRepository repository;

  @GetMapping
  public List<User> index() {
    return repository.findAll();
  }
}
