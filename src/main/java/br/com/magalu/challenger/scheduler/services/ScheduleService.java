package br.com.magalu.challenger.scheduler.services;

import br.com.magalu.challenger.scheduler.applications.dtos.ScheduleDto;
import br.com.magalu.challenger.scheduler.domains.entities.Schedule;
import br.com.magalu.challenger.scheduler.domains.entities.ScheduleStatus;
import br.com.magalu.challenger.scheduler.domains.repositories.ScheduleRepository;
import br.com.magalu.challenger.scheduler.services.utils.ScheduleMapper;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@Transactional
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class ScheduleService implements ApiService<ScheduleDto> {

  private final ScheduleRepository scheduleRepository;
  private final RecipientService recipientService;

  private URI getUriToStatusCode200(final UUID id) {
    return ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(id)
        .toUri();
  }

  @Override
  public Optional<URI> add(final ScheduleDto dto) {
    return recipientService
        .addRecipient(dto.getRecipient())
        .map(recipient -> ScheduleMapper.fromDtoToEntity(dto, recipient))
        .map(this.scheduleRepository::save)
        .map(Schedule::getId)
        .map(this::getUriToStatusCode200);
  }

  @Override
  public Optional<ScheduleDto> getScheduleById(String id) {
    return this.scheduleRepository
        .findById(UUID.fromString(id))
        .map(ScheduleMapper::fromEntityToDto);
  }

  @Override
  public Optional<ScheduleDto> deleteScheduleById(String id) {
    return this.scheduleRepository.findByIdAndScheduleStatus(
        UUID.fromString(id), ScheduleStatus.SCHEDULED)
        .map(this::changeScheduleStatusToExcluded)
        .map(this.scheduleRepository::save)
        .map(ScheduleMapper::fromEntityToDto);
  }

  private Schedule changeScheduleStatusToExcluded(Schedule schedule) {
    schedule.setScheduleStatus(ScheduleStatus.EXCLUDED);
    return schedule;
  }
}
