package com.github.throyer.rabbitmq.errors;

public class UnretryableFailureException extends RuntimeException {
  public UnretryableFailureException(String message) {
    super(message);
  }

  public UnretryableFailureException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnretryableFailureException(Throwable cause) {
    super(cause);
  }

  public UnretryableFailureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
