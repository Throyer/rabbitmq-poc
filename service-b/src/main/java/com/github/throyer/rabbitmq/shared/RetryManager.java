package com.github.throyer.rabbitmq.shared;

import static com.github.throyer.rabbitmq.shared.ChannelManager.create;
import static com.github.throyer.rabbitmq.utils.Time.elapsedMilliseconds;
import static java.lang.System.nanoTime;
import static java.util.Objects.isNull;

import java.io.IOException;

import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import com.github.throyer.rabbitmq.errors.NotRetryableFailureException;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RetryManager<T> implements ChannelAwareMessageListener {
  public RetryManager(SimpleRetryListener<T> listener) {
    super();
    this.listener = listener;
  }

  private final SimpleRetryListener<T> listener;

  @Override
  public void onMessage(org.springframework.amqp.core.Message content, Channel channel) throws Exception {
    var manager = create(
      channel,
      content.getMessageProperties(),
      listener.getSettings().getMaxRetryAttempts()
    );

    var body = parse(manager, content, listener);
    
    if (isNull(body)) {
      return;
    }

    var message = new Message<>(body, manager);

    try {
      if (!listener.getSettings().isEnabled()) {
        manager.doAck();
        return;
      }

      log.info("processamento de mensagem iniciado");
      var startTime = nanoTime();

      listener.onMessage(message);
      manager.doAck();

      var endTime = nanoTime();
      log.info("processamento de mensagem finalizado em {}", elapsedMilliseconds(startTime, endTime));

    } catch (NotRetryableFailureException exception) {

      log.error("falha não re-tentável, erro: {}", exception.getMessage());
      listener.onMaxRetryAttempts(new Fail<>(exception, body));
      manager.doAck();

    } catch (Exception exception) {

      if (manager.alreadyReachedMaxOfAttempts()) {

        log.error("limite de tentativas excedido, tentativa: {}, erro: {}", manager.getCurrentAttempt(), exception.getMessage());
        listener.onMaxRetryAttempts(new Fail<>(exception, body));
        manager.doAck();

      } else {
        log.error("erro durante processamento de mensagem, tentativa: {}, erro {}", manager.getCurrentAttempt(), exception.getMessage());
        manager.doReject();
      }
    }
  }

  public static <T> T parse(
    ChannelManager manager,
    org.springframework.amqp.core.Message message,
    SimpleRetryListener<T> listener
  ) throws IOException {
    try {
      var content = new String(message.getBody());
      return listener.parse(content);
    } catch (Exception exception) {
      manager.doAck();
      listener.onMaxRetryAttempts(new Fail<>(exception, null));
      log.error("Erro ao fazer o parse. Descartando da mensagem. {}", exception.getMessage());
      return null;
    }
  }
}
