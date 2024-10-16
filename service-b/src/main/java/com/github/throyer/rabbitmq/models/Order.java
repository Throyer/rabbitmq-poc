package com.github.throyer.rabbitmq.models;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Valid
public class Order {
  @NotNull
  private Long id;
  
  @NotEmpty
  private String product;
}
