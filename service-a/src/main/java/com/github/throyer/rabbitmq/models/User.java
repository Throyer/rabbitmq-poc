package com.github.throyer.rabbitmq.models;

public class User {

  public User() { }

  public User(String name) {
    this.name = name;
  }

  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}