package com.github.throyer.rabbitmq.shared;

import com.rabbitmq.client.Channel;
import lombok.Getter;

import java.io.IOException;
import java.util.Map;

import static com.github.throyer.rabbitmq.utils.AmqpUtils.extractDeathCount;

public class RetryManager {
  

  private final Channel channel;
  private final long tag;
  private final Map<String, ?> deathHeader;

  @Getter
  private final long maxRetryAttempts;

  private RetryManager(
    Channel channel,
    long tag,
    Map<String, ?> deathHeader,
    long maxRetryAttempts
  ) {
    this.channel = channel;
    this.tag = tag;
    this.deathHeader = deathHeader;
    this.maxRetryAttempts = maxRetryAttempts;
  }

  public static RetryManager create(
    Channel channel,
    long tag,
    Map<String, ?> deathHeader,
    long maxRetryAttempts
  ) {
    return new RetryManager(channel, tag, deathHeader, maxRetryAttempts);
  }

  public void doAck() throws IOException {
    channel.basicAck(tag, false);
  }

  public void doReject() throws IOException {
    channel.basicReject(tag, false);
  }

  public long getDeathCount() {
    return extractDeathCount(deathHeader) + 1;
  }

  public Boolean alreadyReachedMaxOfAttempts() {
    var attempts = getDeathCount();
    return attempts >= maxRetryAttempts;
  }
}
