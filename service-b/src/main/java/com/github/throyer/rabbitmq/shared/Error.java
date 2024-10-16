package com.github.throyer.rabbitmq.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import javax.validation.ConstraintViolation;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
public class Error {
  @JsonInclude(NON_NULL)
  private final String field;
  
  private final String message;

  public Error(String message) {
    this.field = null;
    this.message = message;
  }
  
  public Error(ConstraintViolation<?> violation) {
    this.field = violation.getPropertyPath().toString();
    this.message = violation.getMessage();
  }
}
