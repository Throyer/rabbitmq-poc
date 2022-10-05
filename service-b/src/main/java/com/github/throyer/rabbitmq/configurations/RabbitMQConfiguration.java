package com.github.throyer.rabbitmq.configurations;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
  @Bean("rabbitmq-connection")
  public CachingConnectionFactory connection(
    @Value("${spring.rabbitmq.host}") String host,

    @Value("${spring.rabbitmq.port}") int port,

    @Value("${spring.rabbitmq.username}") String username,

    @Value("${spring.rabbitmq.password}") String password,

    @Value("${spring.rabbitmq.virtual-host}") String virtualHost
  ) {
    var connection = new CachingConnectionFactory();

    connection.setHost(host);
    connection.setPort(port);
    connection.setUsername(username);
    connection.setPassword(password);
    connection.setVirtualHost(virtualHost);

    return connection;
  }

  @Bean
  public MessageConverter converter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean(name = "rabbitmq-container")
  public SimpleRabbitListenerContainerFactory container(
    @Qualifier("rabbitmq-connection")
    ConnectionFactory connection,

    SimpleRabbitListenerContainerFactoryConfigurer configurer
  ) {
    var container = new SimpleRabbitListenerContainerFactory();
    container.setMessageConverter(converter());
    container.setConnectionFactory(connection);
    container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
    return container;
  }

  @Bean(name = "rabbitmq-template")
  public RabbitTemplate customerRabbitTemplate(
    @Qualifier("rabbitmq-connection") ConnectionFactory factory
  ) {
    final var template = new RabbitTemplate(factory);
    template.setMessageConverter(converter());
    return template;
  }
}
