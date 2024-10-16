package com.github.throyer.rabbitmq.models;

import com.github.throyer.rabbitmq.dtos.UserDto;
import lombok.Getter;
import lombok.Setter;

import static java.lang.String.valueOf;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.Optional;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {
  public User() { }

  public User(UserDto dto) {
    this.name = dto.getName();
  }

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;
    
  @Override
  public String toString() {
    return valueOf(name);
  }
}
