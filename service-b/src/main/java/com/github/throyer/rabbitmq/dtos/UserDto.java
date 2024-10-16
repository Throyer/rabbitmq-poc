package com.github.throyer.rabbitmq.dtos;

import com.github.throyer.rabbitmq.models.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
  private Long id;

  @NotNull
  private String name;
}
