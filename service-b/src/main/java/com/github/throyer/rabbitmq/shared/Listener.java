package com.github.throyer.rabbitmq.shared;

import com.github.throyer.rabbitmq.errors.UnretryableFailureException;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.github.throyer.rabbitmq.shared.RetryManager.create;
import static com.github.throyer.rabbitmq.utils.Time.elapsedMilliseconds;
import static java.lang.System.nanoTime;
import static java.util.Objects.isNull;

@Slf4j
public abstract class Listener<T> {
  public void receive(
    Boolean enabled,
    Integer maxRetryAttempts,
    Channel channel,
    Long tag,
    Map<String, ?> deaths,
    org.springframework.amqp.core.Message content,
    Function<String, T> parser,
    Consumer<Message<T>> process,
    Consumer<DlqMessage<T>> onMaxRetryAttempts
  ) throws IOException {
    var manager = create(channel, tag, deaths, maxRetryAttempts);

    var body = parse(manager, parser, content, onMaxRetryAttempts);
    
    if (isNull(body)) {
      return;
    }

    var message = new Message<>(body, manager);

    try {
      if (!enabled) {
        manager.doAck();
        return;
      }

      log.info("processamento de mensagem iniciado");
      var startTime = nanoTime();

      process.accept(message);
      manager.doAck();

      var endTime = nanoTime();
      log.info("processamento de mensagem finalizado em {}", elapsedMilliseconds(startTime, endTime));

    } catch (UnretryableFailureException exception) {

      log.error("falha não re-tentável, erro: {}", exception.getMessage());
      onMaxRetryAttempts.accept(new DlqMessage<>(exception, body));
      manager.doAck();

    } catch (Exception exception) {

      if (manager.alreadyReachedMaxOfAttempts()) {

        log.error("limite de tentativas excedido, tentativa: {}, erro: {}", manager.getDeathCount(), exception.getMessage());
        onMaxRetryAttempts.accept(new DlqMessage<>(exception, body));
        manager.doAck();

      } else {
        log.error("erro durante processamento de mensagem, tentativa: {}, erro {}", manager.getDeathCount(), exception.getMessage());
        manager.doReject();
      }
    }
  }

  public static <T> T parse(
    RetryManager manager,
    Function<String, T> parser,
    org.springframework.amqp.core.Message message,
    Consumer<DlqMessage<T>> onMaxRetryAttempts
  ) throws IOException {
    try {
      var content = new String(message.getBody());
      return parser.apply(content);
    } catch (Exception exception) {
      manager.doAck();
      onMaxRetryAttempts.accept(new DlqMessage<>(exception, null));
      log.error("Erro ao fazer o parse. Descartando da mensagem.");
      return null;
    }
  }
}
