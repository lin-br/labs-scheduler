package br.com.magalu.challenger.scheduler.services.utils;

import br.com.magalu.challenger.scheduler.applications.dtos.ScheduleDto;
import br.com.magalu.challenger.scheduler.domains.entities.Recipient;
import br.com.magalu.challenger.scheduler.domains.entities.Schedule;
import br.com.magalu.challenger.scheduler.domains.entities.ScheduleStatus;
import br.com.magalu.challenger.scheduler.domains.entities.SendType;
import br.com.magalu.challenger.scheduler.services.exceptions.ScheduleTypeException;

public final class ScheduleMapper {

  public static Schedule fromDtoToEntity(ScheduleDto dto, Recipient recipient) {
    return Schedule.builder()
        .sendDate(dto.getDate())
        .recipient(recipient)
        .sendType(getSendType(dto.getType()))
        .message(dto.getMessage())
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
}
