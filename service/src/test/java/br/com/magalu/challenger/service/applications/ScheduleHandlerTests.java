package br.com.magalu.challenger.service.applications;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.magalu.challenger.commons.Scheduler.Request;
import br.com.magalu.challenger.commons.Scheduler.Response;
import br.com.magalu.challenger.commons.Scheduler.Schedule;
import br.com.magalu.challenger.service.services.AsService;
import com.google.protobuf.InvalidProtocolBufferException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScheduleHandlerTests {

  private final UUID id = UUID.fromString("df368c62-9054-4b39-ab7a-46c172d6fc58");
  private AsService<Schedule, Response> service;
  private ScheduleHandler handler;

  @BeforeEach
  void setup() {
    this.service = mock(AsService.class);
    this.handler = new ScheduleHandler(this.service);
  }

  @Test
  void shouldAddSchedule() throws InvalidProtocolBufferException {
    when(this.service.add(any())).thenReturn(Optional.of(this.id));

    final Request request = Request.newBuilder().setPayload(Schedule.newBuilder().build()).build();

    final byte[] bytes = this.handler.addSchedule(request.toByteArray());

    final String idFromService = Response.parseFrom(bytes).getId();

    verify(this.service, times(1)).add(any(Schedule.class));
    assertNotNull(idFromService);
    assertEquals(this.id.toString(), idFromService);
  }

  @Test
  void shoulReturnByteArrayEmptyIfAddMethodNotWork() throws InvalidProtocolBufferException {
    when(this.service.add(any())).thenReturn(Optional.empty());

    final Request request = Request.newBuilder().setPayload(Schedule.newBuilder().build()).build();

    final byte[] bytes = this.handler.addSchedule(request.toByteArray());

    verify(this.service, times(1)).add(any(Schedule.class));
    assertEquals(0, bytes.length);
  }

  @Test
  void shouldGetSchedule() throws InvalidProtocolBufferException {
    final Response response =
        Response.newBuilder().setSchedule(Schedule.newBuilder().setMessage("it is a test")).build();

    when(this.service.getScheduleById(any())).thenReturn(Optional.of(response));

    final Request request = Request.newBuilder().setId(this.id.toString()).build();

    final byte[] bytes = this.handler.getSchedule(request.toByteArray());

    final String status = Response.parseFrom(bytes).getStatus();

    verify(this.service, times(1)).getScheduleById(anyString());
    assertTrue(bytes.length > 0);
  }

  @Test
  void shoulReturnByteArrayEmptyIfGetScheduleMethodNotWork() throws InvalidProtocolBufferException {
    when(this.service.getScheduleById(any())).thenReturn(Optional.empty());

    final Request request = Request.newBuilder().setId(this.id.toString()).build();

    final byte[] bytes = this.handler.getSchedule(request.toByteArray());

    verify(this.service, times(1)).getScheduleById(anyString());
    assertEquals(0, bytes.length);
  }

  @Test
  void shouldDelSchedule() throws InvalidProtocolBufferException {
    final Response response =
        Response.newBuilder().setSchedule(Schedule.newBuilder().setMessage("it is a test")).build();

    when(this.service.deleteScheduleById(any())).thenReturn(Optional.of(response));

    final Request request = Request.newBuilder().setId(this.id.toString()).build();

    final byte[] bytes = this.handler.delSchedule(request.toByteArray());

    final String status = Response.parseFrom(bytes).getStatus();

    verify(this.service, times(1)).deleteScheduleById(anyString());
    assertTrue(bytes.length > 0);
  }

  @Test
  void shoulReturnByteArrayEmptyIfDelScheduleMethodNotWork() throws InvalidProtocolBufferException {
    when(this.service.deleteScheduleById(any())).thenReturn(Optional.empty());

    final Request request = Request.newBuilder().setId(this.id.toString()).build();

    final byte[] bytes = this.handler.delSchedule(request.toByteArray());

    verify(this.service, times(1)).deleteScheduleById(anyString());
    assertEquals(0, bytes.length);
  }
}
