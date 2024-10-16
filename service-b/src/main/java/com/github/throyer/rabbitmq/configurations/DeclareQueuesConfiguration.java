package com.github.throyer.rabbitmq.configurations;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import com.github.throyer.rabbitmq.shared.DeclareQueue;
import com.github.throyer.rabbitmq.shared.RabbitMQProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class DeclareQueuesConfiguration {
  private final RabbitAdmin admin;
  private final RabbitMQProperties properties;
  private final List<DeclareQueue> declares;

  public DeclareQueuesConfiguration(
    @Qualifier("rabbitmq-connection") ConnectionFactory connection,
    RabbitMQProperties properties,
    List<DeclareQueue> declares
  ) {
    this.admin = new RabbitAdmin(connection);
    this.properties = properties;
    this.declares = declares;
  }

  @PostConstruct
  public void declare() {
    declares.forEach(configuration -> {
      try {
        var settings = properties.get(configuration.getAlias());
        configuration.declare(settings, admin);      
      } catch (Exception exception) {
        log.error(
          "não foi possível declarar as filas do rabbitmq. '{}', {}",
          configuration.getAlias(),
          exception.getMessage()
        );
      }
    });
  }
}
