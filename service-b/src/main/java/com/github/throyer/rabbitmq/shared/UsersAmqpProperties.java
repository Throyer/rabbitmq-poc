package com.github.throyer.rabbitmq.shared;

import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "rabbitmq.user")
public class UsersAmqpProperties {
  @NestedConfigurationProperty
  private ConnectionSettings connection;

  @NestedConfigurationProperty
  private Set<QueueSettings> queues;

  public QueueSettings get(String alias) {
    return queues.stream().filter(queue -> queue.getAlias().equalsIgnoreCase(alias))
      .findFirst()
      .orElseThrow(() -> new RuntimeException(String.format("não foi possível localizar as configurações para a fila \"%s\"", alias)));
  }
}
