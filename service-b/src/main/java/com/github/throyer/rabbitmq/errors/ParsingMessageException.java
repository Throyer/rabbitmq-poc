package com.github.throyer.rabbitmq.errors;

public class ParsingMessageException extends RuntimeException {
  public ParsingMessageException(String message, Throwable cause) {
    super(message, cause);
  }

  public ParsingMessageException(String message) {
    super(message);
  }
}
