package com.github.throyer.rabbitmq.controllers;

import static org.springframework.http.ResponseEntity.ok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.throyer.rabbitmq.models.User;
import com.github.throyer.rabbitmq.services.UsersService;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

  @Autowired
  private UsersService service;

  @PostMapping
  public ResponseEntity<User> update(@RequestBody @Validated User body) {
      var user = service.create(body.getName());
      return ok(user);
  }
}
