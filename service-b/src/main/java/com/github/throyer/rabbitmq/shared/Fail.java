package com.github.throyer.rabbitmq.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

@Getter
public class Fail<T> {
  @JsonIgnore
  private final Exception cause;  
  private final String error;
  private final String createdAt;
  private final T body;

  public Fail(
    Exception exception,
    T body
  ) {
    this.cause = exception;
    this.error = exception.getMessage();
    this.createdAt = LocalDateTime.now().format(ISO_DATE_TIME);
    this.body = body;
  }
}
