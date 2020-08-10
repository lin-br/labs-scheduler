package br.com.magalu.challenger.service.services;

import br.com.magalu.challenger.commons.Scheduler;
import br.com.magalu.challenger.commons.Scheduler.Response;
import br.com.magalu.challenger.service.domains.entities.Schedule;
import br.com.magalu.challenger.service.domains.entities.ScheduleStatus;
import br.com.magalu.challenger.service.domains.repositories.ScheduleRepository;
import br.com.magalu.challenger.service.services.utils.ServiceScheduleMapper;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class ScheduleService implements AsService<Scheduler.Schedule, Response> {

  private final ScheduleRepository scheduleRepository;
  private final RecipientService recipientService;

  @Override
  public Optional<UUID> add(final Scheduler.Schedule schedule) {
    return recipientService
        .addRecipient(schedule.getRecipient())
        .map(recipient -> ServiceScheduleMapper.toEntity(schedule, recipient))
        .map(this.scheduleRepository::save)
        .map(Schedule::getId);
  }

  @Override
  public Optional<Response> getScheduleById(String id) {
    return this.scheduleRepository
        .findById(UUID.fromString(id))
        .map(ServiceScheduleMapper::toProtobuf);
  }

  @Override
  public Optional<Response> deleteScheduleById(String id) {
    return this.scheduleRepository
        .findByIdAndScheduleStatus(UUID.fromString(id), ScheduleStatus.SCHEDULED)
        .map(this::changeScheduleStatusToExcluded)
        .map(this.scheduleRepository::save)
        .map(ServiceScheduleMapper::toProtobuf);
  }

  private Schedule changeScheduleStatusToExcluded(Schedule schedule) {
    schedule.setScheduleStatus(ScheduleStatus.EXCLUDED);
    return schedule;
  }
}
