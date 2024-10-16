package com.github.throyer.rabbitmq.errors;

import com.github.throyer.rabbitmq.shared.Error;
import lombok.Getter;

import java.util.Collection;

@Getter
public class ValidationException extends RuntimeException {
  private final Collection<Error> errors;

  public ValidationException(Collection<Error> errors, String message) {
    super(message);
    this.errors = errors;
  }
}
