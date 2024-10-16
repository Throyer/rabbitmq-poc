package com.github.throyer.rabbitmq.errors;

public class NotRetryableFailureException extends RuntimeException {
  public NotRetryableFailureException(String message) {
    super(message);
  }

  public NotRetryableFailureException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotRetryableFailureException(Throwable cause) {
    super(cause);
  }

  public NotRetryableFailureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
