package com.github.throyer.rabbitmq.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.throyer.rabbitmq.models.User;
import com.github.throyer.rabbitmq.repositories.UsersRepository;
import com.github.throyer.rabbitmq.shared.Page;

@RequestMapping("/api/v1/users")
@RestController
public class UsersController {

  @Autowired
  private UsersRepository repository;

  @GetMapping
  public Page<User> index(Pageable pageable) {
    return Page.of(repository.findAll(pageable));
  }
}
