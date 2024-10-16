package com.github.throyer.rabbitmq.utils;

import static java.util.Objects.nonNull;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class AmqpUtils {
  public static final String RABBITMQ_DEATH_HEADER_NAME = "x-death";
  public static final String DEFAULT_ALGORITHM = "TLSv1.2";
  public static final String DEATH_COUNT_KEY_NAME = "count";
  
  private AmqpUtils() { }

  public static Boolean hasExceededRetryLimit(HashMap<String, ?> properties, Long maxAttempts) {
    if (nonNull(properties) && !properties.isEmpty()) {
      Long count = (Long) properties.get("count");
      String queue = (String) properties.get("queue");
      log.info("count: {}, max-attempts: {}, queue: {}", count, maxAttempts, queue);
      return count >= maxAttempts;
    }
    return false;
  }

  public static Long extractDeathCount(Map<String, ?> headers) {
    if (nonNull(headers) && !headers.isEmpty()) {
      return (Long) headers.get(DEATH_COUNT_KEY_NAME);
    }
    return 0L;
  }
}
