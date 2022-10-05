package com.github.throyer.rabbitmq.utils;

import static java.util.Objects.nonNull;

import java.util.HashMap;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class RetryUtils {
  private RetryUtils() { }

  public static Boolean hasExceededRetryLimit(HashMap<String, ?> properties, Long maxAttempts) {
    if (nonNull(properties) && !properties.isEmpty()) {
      Long count = (Long) properties.get("count");
      String queue = (String) properties.get("queue");
      log.info("count: {}, max-attempts: {}, queue: {}", count, maxAttempts, queue);
      return count >= maxAttempts;
    }
    return false;
  }
}
