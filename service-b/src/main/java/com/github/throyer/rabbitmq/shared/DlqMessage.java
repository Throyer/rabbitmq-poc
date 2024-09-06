package com.github.throyer.rabbitmq.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

@Getter
public class DlqMessage<T> {
  @JsonIgnore
  private final Exception exception;
  
  private final String error;
  private final String createdAt;
  private final T body;

  public DlqMessage(
    Exception exception,
    T body
  ) {
    this.exception = exception;
    this.error = exception.getMessage();
    this.createdAt = LocalDateTime.now().format(ISO_DATE_TIME);
    this.body = body;
  }
}
