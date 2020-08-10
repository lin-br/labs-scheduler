package br.com.magalu.challenger.api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.magalu.challenger.api.applications.dtos.ScheduleDto;
import br.com.magalu.challenger.api.applications.dtos.SendTypeDto;
import br.com.magalu.challenger.commons.Scheduler.Request;
import br.com.magalu.challenger.commons.Scheduler.Response;
import br.com.magalu.challenger.commons.Scheduler.Schedule;
import br.com.magalu.challenger.commons.Scheduler.Schedule.SendType;
import br.com.magalu.challenger.commons.applications.SchedulerProperties;
import com.google.protobuf.Timestamp;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScheduleServiceTests {

  private final UUID id = UUID.fromString("df368c62-9054-4b39-ab7a-46c172d6fc58");
  private BrokerService<Request, Response> brokerService;
  private ScheduleService service;

  @BeforeEach
  void setup() {
    this.brokerService = mock(BrokerService.class);
    this.service = new ScheduleService(this.brokerService);
  }

  @Test
  void shouldAddSchedule() {
    when(this.brokerService.sendMessageAndReceive(anyString(), any()))
        .thenReturn(Optional.of(Response.newBuilder().setId(id.toString()).build()));

    final ScheduleDto dto =
        ScheduleDto.builder()
            .message("it is a test")
            .type(SendTypeDto.WHATSAPP)
            .recipient("it is a recipient")
            .date(Calendar.getInstance())
            .build();

    final UUID uuid = this.service.add(dto).get();

    verify(this.brokerService, times(1))
        .sendMessageAndReceive(eq(SchedulerProperties.QUEUE_ADD_SCHEDULE), any(Request.class));
    assertEquals(this.id, uuid);
  }

  @Test
  void shouldGetScheduleById() {
    final Schedule schedule =
        Schedule.newBuilder()
            .setMessage("it is a test")
            .setType(SendType.EMAIL)
            .setSendDate(Timestamp.getDefaultInstance())
            .build();
    when(this.brokerService.sendMessageAndReceive(anyString(), any()))
        .thenReturn(
            Optional.of(Response.newBuilder().setSchedule(schedule).build()));

    final ScheduleDto response = this.service.getScheduleById(this.id.toString()).get();

    verify(this.brokerService, times(1))
        .sendMessageAndReceive(eq(SchedulerProperties.QUEUE_GET_SCHEDULE), any(Request.class));
    assertEquals(schedule.getType().name(), response.getType().name());
    assertEquals(schedule.getMessage(), response.getMessage());
  }

  @Test
  void shouldDeleteScheduleById() {
    final Schedule schedule =
        Schedule.newBuilder()
            .setMessage("it is a test")
            .setType(SendType.EMAIL)
            .setSendDate(Timestamp.getDefaultInstance())
            .build();
    when(this.brokerService.sendMessageAndReceive(anyString(), any()))
        .thenReturn(
            Optional.of(Response.newBuilder().setSchedule(schedule).setStatus("EXCLUDED").build()));

    final ScheduleDto response = this.service.deleteScheduleById(this.id.toString()).get();

    verify(this.brokerService, times(1))
        .sendMessageAndReceive(eq(SchedulerProperties.QUEUE_DEL_SCHEDULE), any(Request.class));
    assertEquals(schedule.getType().name(), response.getType().name());
    assertEquals(schedule.getMessage(), response.getMessage());
    assertEquals("EXCLUDED", response.getStatus());
  }
}
