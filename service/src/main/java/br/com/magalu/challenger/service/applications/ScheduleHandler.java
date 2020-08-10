package br.com.magalu.challenger.service.applications;

import br.com.magalu.challenger.commons.Scheduler;
import br.com.magalu.challenger.commons.Scheduler.Request;
import br.com.magalu.challenger.commons.Scheduler.Response;
import br.com.magalu.challenger.commons.Scheduler.Schedule;
import br.com.magalu.challenger.commons.applications.SchedulerProperties;
import br.com.magalu.challenger.service.services.AsService;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ScheduleHandler {

  private final AsService<Schedule, Response> service;

  @RabbitListener(queues = SchedulerProperties.QUEUE_ADD_SCHEDULE)
  public byte[] addSchedule(byte[] payload) throws InvalidProtocolBufferException {
    final Request request = Request.parseFrom(payload);
    final Schedule schedule = Schedule.newBuilder(request.getPayload()).build();

    return this.service
        .add(schedule)
        .map(
            uuid ->
                Scheduler.Response.newBuilder()
                    .setSchedule(schedule)
                    .setId(uuid.toString())
                    .build()
                    .toByteArray())
        .orElse(Scheduler.Response.getDefaultInstance().toByteArray());
  }

  @RabbitListener(queues = SchedulerProperties.QUEUE_GET_SCHEDULE)
  public byte[] getSchedule(byte[] payload) throws InvalidProtocolBufferException {
    return this.service
        .getScheduleById(getIdFromRequest(payload))
        .map(Response::toByteArray)
        .orElse(Scheduler.Response.getDefaultInstance().toByteArray());
  }

  private String getIdFromRequest(byte[] payload) throws InvalidProtocolBufferException {
    return Request.parseFrom(payload).getId();
  }

  @RabbitListener(queues = SchedulerProperties.QUEUE_DEL_SCHEDULE)
  public byte[] delSchedule(byte[] payload) throws InvalidProtocolBufferException {
    return this.service
        .deleteScheduleById(getIdFromRequest(payload))
        .map(Response::toByteArray)
        .orElse(Scheduler.Response.getDefaultInstance().toByteArray());
  }
}
