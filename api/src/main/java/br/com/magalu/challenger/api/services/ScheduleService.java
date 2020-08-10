package br.com.magalu.challenger.api.services;

import br.com.magalu.challenger.api.applications.dtos.ScheduleDto;
import br.com.magalu.challenger.api.services.utils.ApiScheduleMapper;
import br.com.magalu.challenger.commons.Scheduler.Request;
import br.com.magalu.challenger.commons.Scheduler.Response;
import br.com.magalu.challenger.commons.applications.SchedulerProperties;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class ScheduleService implements ApiService<ScheduleDto> {

  private final BrokerService<Request, Response> brokerService;

  @Override
  public Optional<UUID> add(final ScheduleDto dto) {
    final Request request =
        Request.newBuilder().setPayload(ApiScheduleMapper.toProtobuf(dto)).build();

    return this.brokerService
        .sendMessageAndReceive(SchedulerProperties.QUEUE_ADD_SCHEDULE, request)
        .map(Response::getId)
        .map(UUID::fromString);
  }

  @Override
  public Optional<ScheduleDto> getScheduleById(String id) {
    final Request request = Request.newBuilder().setId(id).build();

    return this.brokerService
        .sendMessageAndReceive(SchedulerProperties.QUEUE_GET_SCHEDULE, request)
        .map(response -> ApiScheduleMapper.toDto(response.getSchedule(), response.getStatus()));
  }

  @Override
  public Optional<ScheduleDto> deleteScheduleById(String id) {
    final Request request = Request.newBuilder().setId(id).build();

    return this.brokerService
        .sendMessageAndReceive(SchedulerProperties.QUEUE_DEL_SCHEDULE, request)
        .map(response -> ApiScheduleMapper.toDto(response.getSchedule(), response.getStatus()));
  }
}
