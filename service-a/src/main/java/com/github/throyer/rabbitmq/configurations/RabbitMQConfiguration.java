package com.github.throyer.rabbitmq.configurations;

import static org.springframework.amqp.core.BindingBuilder.bind;
import static org.springframework.amqp.core.ExchangeBuilder.fanoutExchange;
import static org.springframework.amqp.core.QueueBuilder.durable;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
  
  @Bean("rabbitmq-connection")
  public CachingConnectionFactory factory(
    @Value("${spring.rabbitmq.host}")
    String host,

    @Value("${spring.rabbitmq.port}")
    int port,

    @Value("${spring.rabbitmq.username}")
    String username,

    @Value("${spring.rabbitmq.password}")
    String password,

    @Value("${spring.rabbitmq.virtual-host}")
    String virtualHost
  ) {
    var factory = new CachingConnectionFactory();

    factory.setHost(host);
    factory.setPort(port);
    factory.setUsername(username);
    factory.setPassword(password);
    factory.setVirtualHost(virtualHost);
    
    return factory;
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

  @Bean(name = "rabbitmq-template")
	public RabbitTemplate customerRabbitTemplate(
    @Qualifier("rabbitmq-connection")
    ConnectionFactory factory
  ) {
    final var template = new RabbitTemplate(factory);
    template.setMessageConverter(converter());
    return template;
	}
}
