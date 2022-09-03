package com.github.throyer.rabbitmq.models;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.util.Optional;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;

public class User {
  public User() { }

  public User(String name) {
    this.name = name;
  }

  @JsonInclude(NON_NULL)
  private Long id;
  
  @NotEmpty
  private String name;
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
      return Optional.ofNullable(name).orElse("null");
  }
}
