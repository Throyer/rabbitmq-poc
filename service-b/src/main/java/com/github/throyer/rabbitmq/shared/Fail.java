package com.github.throyer.rabbitmq.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.throyer.rabbitmq.errors.ValidationException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

@Getter
public class Fail<T> {
  @JsonIgnore
  private final Exception cause;
  
  private final Collection<Error> errors;
  private final String createdAt;
  private final T body;

  public Fail(
    Exception exception,
    T body
  ) {
    this.cause = exception;
    this.errors = List.of(new Error(exception.getMessage()));    
    this.createdAt = LocalDateTime.now().format(ISO_DATE_TIME);
    this.body = body;
  }

  public Fail(
    Collection<Error> errors,
    T body
  ) {
    this.cause = null;
    this.errors = errors;
    this.createdAt = LocalDateTime.now().format(ISO_DATE_TIME);
    this.body = body;
  }
}
