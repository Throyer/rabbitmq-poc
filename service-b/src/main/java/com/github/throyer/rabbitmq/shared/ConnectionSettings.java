package com.github.throyer.rabbitmq.shared;

import static java.util.Optional.ofNullable;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectionSettings {
  private static final String DEFAULT_ALGORITHM = "TLSv1.2";

  private String host;
  private Integer port;
  private String username;
  private String password;
  private String virtualHost;
  private SSLSettings sslSettings;
  private ListenerSettings listenerSettings;

  public CachingConnectionFactory getConnectionFactory() throws NoSuchAlgorithmException, KeyManagementException {
    var factory = new CachingConnectionFactory();

    factory.setHost(this.host);
    factory.setPort(this.port);
    factory.setUsername(this.username);
    factory.setPassword(this.password);
    factory.setVirtualHost(this.virtualHost);

    var ssl = this.sslSettings;
    
    if (ssl.isEnabled()) {
      factory.getRabbitConnectionFactory()
        .useSslProtocol(ofNullable(ssl.getAlgorithm())
          .orElse(DEFAULT_ALGORITHM));
    }

    return factory;
  }
}
