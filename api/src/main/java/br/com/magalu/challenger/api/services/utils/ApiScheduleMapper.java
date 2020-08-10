package br.com.magalu.challenger.api.services.utils;

import br.com.magalu.challenger.api.applications.dtos.ScheduleDto;
import br.com.magalu.challenger.api.applications.dtos.SendTypeDto;
import br.com.magalu.challenger.commons.Scheduler;
import br.com.magalu.challenger.commons.Scheduler.Schedule;
import br.com.magalu.challenger.commons.utils.WorkingDate;

public final class ApiScheduleMapper {

  public static Schedule toProtobuf(ScheduleDto dto) {
    return Schedule.newBuilder()
        .setSendDate(WorkingDate.getTimestampFromCalendar(dto.getDate()))
        .setRecipient(dto.getRecipient())
        .setType(Scheduler.Schedule.SendType.valueOf(dto.getType().name()))
        .setMessage(dto.getMessage())
        .build();
  }

  public static ScheduleDto toDto(Schedule schedule, String status) {
    return ScheduleDto.builder()
        .date(WorkingDate.getCalendarFromTimstamp(schedule.getSendDate()))
        .message(schedule.getMessage())
        .recipient(schedule.getRecipient())
        .type(SendTypeDto.valueOf(schedule.getType().name()))
        .status(status)
        .build();
  }
}
