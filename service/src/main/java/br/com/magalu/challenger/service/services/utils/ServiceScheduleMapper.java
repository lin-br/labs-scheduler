package br.com.magalu.challenger.service.services.utils;

import br.com.magalu.challenger.commons.Scheduler;
import br.com.magalu.challenger.commons.utils.WorkingDate;
import br.com.magalu.challenger.service.domains.entities.Recipient;
import br.com.magalu.challenger.service.domains.entities.Schedule;
import br.com.magalu.challenger.service.domains.entities.ScheduleStatus;
import br.com.magalu.challenger.service.domains.entities.SendType;
import br.com.magalu.challenger.service.services.exceptions.ScheduleTypeException;

public class ServiceScheduleMapper {

  public static Schedule toEntity(Scheduler.Schedule protobuf, Recipient recipient) {
    return Schedule.builder()
        .sendDate(WorkingDate.getCalendarFromTimstamp(protobuf.getSendDate()))
        .recipient(recipient)
        .sendType(getSendType(protobuf.getType().name()))
        .message(protobuf.getMessage())
        .scheduleStatus(ScheduleStatus.SCHEDULED)
        .build();
  }

  private static SendType getSendType(String sendType) throws ScheduleTypeException {
    try {
      return SendType.valueOf(sendType.toUpperCase());
    } catch (IllegalArgumentException exception) {
      throw new ScheduleTypeException("O tipo do envio está inválido");
    }
  }

  private static Scheduler.Schedule getScheduleProtobuf(Schedule schedule) {
    return Scheduler.Schedule.newBuilder()
        .setSendDate(WorkingDate.getTimestampFromCalendar(schedule.getSendDate()))
        .setMessage(schedule.getMessage())
        .setRecipient(schedule.getRecipient().getRecipient())
        .setType(Scheduler.Schedule.SendType.valueOf(schedule.getSendType().name()))
        .build();
  }

  public static Scheduler.Response toProtobuf(Schedule schedule) {
    return Scheduler.Response.newBuilder()
        .setSchedule(getScheduleProtobuf(schedule))
        .setStatus(schedule.getScheduleStatus().name())
        .build();
  }
}
