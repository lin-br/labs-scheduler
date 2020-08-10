package br.com.magalu.challenger.api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import br.com.magalu.challenger.commons.Scheduler.Request;
import br.com.magalu.challenger.commons.Scheduler.Response;
import br.com.magalu.challenger.commons.Scheduler.Schedule;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;

public class ApiMessageServiceTests {

  private RabbitMessagingTemplate rabbitMessagingTemplate;
  private DirectExchange directExchange;
  private ApiMessageService messageService;

  @BeforeEach
  void setup() {
    this.rabbitMessagingTemplate = mock(RabbitMessagingTemplate.class);
    this.directExchange = mock(DirectExchange.class);
    this.messageService = new ApiMessageService(this.rabbitMessagingTemplate, this.directExchange);
  }

  @Test
  void shouldSendMessageOnBroker() {
    when(this.directExchange.getName()).thenReturn("exchange");
    when(this.rabbitMessagingTemplate.convertSendAndReceive(
        anyString(), anyString(), any(byte[].class), any()))
        .thenReturn(Response.getDefaultInstance().toByteArray());

    final Optional<Response> optional =
        this.messageService.sendMessageAndReceive("bindingKey", Request.getDefaultInstance());

    Mockito.verify(this.rabbitMessagingTemplate, times(1))
        .convertSendAndReceive(eq("exchange"), eq("bindingKey"), any(byte[].class), any());
  }

  @Test
  void shouldReturnOptionalEmptyIfThrowsInvalidProtocolBufferException() {
    when(this.directExchange.getName()).thenReturn("exchange");
    when(this.rabbitMessagingTemplate.convertSendAndReceive(
        anyString(), anyString(), any(byte[].class), any()))
        .thenReturn(Schedule.getDefaultInstance().toByteArray());

    final Optional<Response> optional =
        this.messageService.sendMessageAndReceive("bindingKey", Request.getDefaultInstance());
    assertEquals(optional.get().getId(), "");
  }
}
