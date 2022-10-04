package com.github.throyer.rabbitmq.configurations;

import static org.springframework.amqp.core.BindingBuilder.bind;
import static org.springframework.amqp.core.ExchangeBuilder.fanoutExchange;
import static org.springframework.amqp.core.QueueBuilder.durable;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
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
  public AmqpAdmin admin(
    @Qualifier("rabbitmq-connection")
    ConnectionFactory factory
  ) {
    var admin = new RabbitAdmin(factory);

    FanoutExchange usersExchange = fanoutExchange("users-exchange")
      .build();

    FanoutExchange usersDeadLetterExchange = fanoutExchange("users-dead-letter-exchange")
      .build();

    var usersQueue = durable("users-queue")
      .deadLetterExchange("users-dead-letter-exchange")
        .build();

    var usersDeadLetterQueue = durable("users-dead-letter-queue")
      .build();

    admin.declareExchange(usersExchange);    
    admin.declareExchange(usersDeadLetterExchange);

    admin.declareQueue(usersQueue);
    admin.declareQueue(usersDeadLetterQueue);
    
    admin.declareBinding(bind(usersQueue).to(usersExchange));
    admin.declareBinding(bind(usersDeadLetterQueue).to(usersDeadLetterExchange));

    return admin;
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
    var factory = new SimpleRabbitListenerContainerFactory();
    factory.setMessageConverter(converter());
    factory.setConnectionFactory(connection);
    return factory;
  }
}
