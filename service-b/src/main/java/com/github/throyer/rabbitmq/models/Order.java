package com.github.throyer.rabbitmq.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {
  private Long id;
  private String product;
}
