package com.github.throyer.rabbitmq.configurations;

import static java.lang.String.format;
import static org.springframework.amqp.core.AcknowledgeMode.MANUAL;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import com.github.throyer.rabbitmq.shared.ListenerSettings;
import com.github.throyer.rabbitmq.shared.RetryManager;
import com.github.throyer.rabbitmq.shared.SimpleRetryListener;

import lombok.extern.slf4j.Slf4j;

import com.github.throyer.rabbitmq.shared.RabbitMQProperties;

@Slf4j
@Configuration
public class CustomListenersConfiguration {
  public CustomListenersConfiguration(
    GenericApplicationContext context,
    @Qualifier("rabbitmq-connection") ConnectionFactory connection,
    RabbitMQProperties properties,
    List<SimpleRetryListener<?>> listeners
  ) {
    this.context = context;
    this.connection = connection;
    this.properties = properties;
    this.listeners = listeners;
  }

  private final GenericApplicationContext context;
  private final ConnectionFactory connection;
  private final RabbitMQProperties properties;
  private final List<SimpleRetryListener<?>> listeners;

  @PostConstruct
  private void create() {
    var listenerSettings = properties
      .getConnection()
        .getListenerSettings();

    listeners.forEach(listener -> {
      try {
        var settings = listener.getSettings();
        if (!settings.isEnabled()) {
          return;
        }
  
        var alias = settings.getAlias();
        var name = format("rabbitmq-listener-%s", alias);
        var type = SimpleMessageListenerContainer.class;
  
        context.registerBean(
          name,
          type,
          () -> container(listener, connection, listenerSettings)
        );
      } catch (Exception exception) {
        log.error("não foi possível fazer a configuração de listener, erro: {}", exception.getMessage());
      }
    });
  }

  public static <T> SimpleMessageListenerContainer container(
    SimpleRetryListener<T> listener,
    ConnectionFactory connectionFactory,
    ListenerSettings listenerSettings
  ) {
    var container = new SimpleMessageListenerContainer();
    var queueSettings = listener.getSettings();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(queueSettings.getQueue().getQueueName());
    container.setDefaultRequeueRejected(listenerSettings.getDefaultRequeueRejected());
    container.setAcknowledgeMode(MANUAL);
    container.setConcurrentConsumers(listenerSettings.getConcurrentConsumers());
    container.setPrefetchCount(listenerSettings.getPrefetch());
    container.setMessageListener(new RetryManager<>(listener));
    return container;
  }
}
