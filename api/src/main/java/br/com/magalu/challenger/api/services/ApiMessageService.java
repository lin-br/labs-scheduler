package br.com.magalu.challenger.api.services;

import br.com.magalu.challenger.commons.Scheduler.Request;
import br.com.magalu.challenger.commons.Scheduler.Response;
import com.google.protobuf.InvalidProtocolBufferException;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class ApiMessageService implements BrokerService<Request, Response> {

  private final RabbitMessagingTemplate rabbitMessagingTemplate;
  private final DirectExchange directExchange;

  @Override
  public Optional<Response> sendMessageAndReceive(String bindingKey, Request request) {
    try {
      final byte[] bytes =
          rabbitMessagingTemplate.convertSendAndReceive(
              directExchange.getName(), bindingKey, request.toByteArray(), byte[].class);

      return Optional.of(Response.parseFrom(bytes));

    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }
}
