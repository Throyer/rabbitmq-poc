package com.github.throyer.rabbitmq.configurations;

import static com.github.throyer.rabbitmq.shared.RetryManager.container;
import static java.lang.String.format;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import com.github.throyer.rabbitmq.shared.SimpleRetryListener;
import com.github.throyer.rabbitmq.shared.UsersAmqpProperties;

@Configuration
public class CustomRabbitmqListenerContainersConfiguration {
  public CustomRabbitmqListenerContainersConfiguration(
    GenericApplicationContext context,
    @Qualifier("rabbitmq-connection") ConnectionFactory connection,
    UsersAmqpProperties properties,
    List<SimpleRetryListener<?>> listeners
  ) {
    this.context = context;
    this.connection = connection;
    this.properties = properties;
    this.listeners = listeners;
  }

  private final GenericApplicationContext context;
  private final ConnectionFactory connection;
  private final UsersAmqpProperties properties;
  private final List<SimpleRetryListener<?>> listeners;

  @PostConstruct
  private void createListeners() {
    var listenerSettings = properties
      .getConnection()
        .getListenerSettings();

    listeners.forEach(listener -> {
      var settings = listener.getSettings();
      var alias = settings.getAlias();
      var name = format("rabbitmq-listener-%s", alias);
      var type = SimpleMessageListenerContainer.class;

      context.registerBean(
        name,
        type,
        () -> container(listener, connection, listenerSettings)
      );
    });
  }
}
