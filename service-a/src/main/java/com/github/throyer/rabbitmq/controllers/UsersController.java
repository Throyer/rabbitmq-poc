package com.github.throyer.rabbitmq.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.throyer.rabbitmq.models.User;
import com.github.throyer.rabbitmq.services.UsersService;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

  @Autowired
  private UsersService service;

  @GetMapping
  public User create(String name) {
    return service.create(name);
  }
}
