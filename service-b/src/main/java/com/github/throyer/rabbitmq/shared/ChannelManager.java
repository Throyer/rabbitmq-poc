package com.github.throyer.rabbitmq.shared;

import com.rabbitmq.client.Channel;
import lombok.Getter;

import org.springframework.amqp.core.MessageProperties;

import static java.util.Objects.isNull;

import java.io.IOException;
import java.util.Map;

import static com.github.throyer.rabbitmq.utils.AmqpUtils.extractDeathCount;

public class ChannelManager {
  
  private final Channel channel;
  private final MessageProperties properties;

  @Getter
  private final long maxRetryAttempts;

  private ChannelManager(
    Channel channel,
    MessageProperties properties,
    long maxRetryAttempts
  ) {
    this.channel = channel;
    this.properties = properties;
    this.maxRetryAttempts = maxRetryAttempts;
  }

  public static ChannelManager create(
    Channel channel,
    MessageProperties properties,
    long maxRetryAttempts
  ) {
    return new ChannelManager(channel, properties, maxRetryAttempts);
  }

  public void doAck() throws IOException {
    channel.basicAck(properties.getDeliveryTag(), false);
  }

  public void doReject() throws IOException {
    channel.basicReject(properties.getDeliveryTag(), false);
  }

  public long getCurrentAttempt() {
    return getDeathCount() + 1;
  }

  public long getDeathCount() {
    if (isNull(properties.getXDeathHeader())) {
      return 0L;
    }
    
    var deathHeaders = properties.getXDeathHeader();
    var expiredDeaths = deathHeaders.stream().filter(this::isDeathHeader)
      .findFirst();

    if (expiredDeaths.isEmpty()) {
      return 0L;
    }
    
    return extractDeathCount(expiredDeaths.get());
  }

  public Boolean alreadyReachedMaxOfAttempts() {
    var attempts = getCurrentAttempt();
    return attempts >= maxRetryAttempts;
  }

  private Boolean isDeathHeader(Map<String, ?> header) {
    if (!header.containsKey("reason")) {
      return false;
    }

    String value = (String) header.get("reason");

    if (isNull(value)) {
      return false;
    }

    return value.equalsIgnoreCase("rejected");
  }
}
