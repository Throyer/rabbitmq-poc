package com.github.throyer.rabbitmq.configurations;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.throyer.rabbitmq.shared.RabbitMQProperties;

@Configuration
public class RabbitMQConfiguration {  
  @Bean("rabbitmq-connection")
  public CachingConnectionFactory connection(
    RabbitMQProperties settings    
  ) throws NoSuchAlgorithmException, KeyManagementException {
    var connection = settings.getConnection();
    return connection.getConnectionFactory();
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
