package br.com.magalu.challenger.service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.magalu.challenger.commons.Scheduler;
import br.com.magalu.challenger.commons.Scheduler.Response;
import br.com.magalu.challenger.service.domains.entities.Recipient;
import br.com.magalu.challenger.service.domains.entities.Schedule;
import br.com.magalu.challenger.service.domains.entities.ScheduleStatus;
import br.com.magalu.challenger.service.domains.entities.SendType;
import br.com.magalu.challenger.service.domains.repositories.ScheduleRepository;
import br.com.magalu.challenger.service.services.exceptions.ScheduleTypeException;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class ScheduleServiceTests {

  private final String recipientString = "test";
  private final UUID id = UUID.fromString("df368c62-9054-4b39-ab7a-46c172d6fc58");
  private ScheduleService service;
  private RecipientService recipientService;
  private ScheduleRepository repository;

  @BeforeEach
  void setup() {
    this.repository = Mockito.spy(ScheduleRepository.class);
    this.recipientService = mock(RecipientService.class);
    this.service = new ScheduleService(this.repository, this.recipientService);
  }

  @Test
  void shouldAddScheduleAndReturnId() {
    when(this.recipientService.addRecipient(recipientString))
        .thenReturn(Optional.of(Recipient.builder().recipient(recipientString).build()));
    when(this.repository.save(any(Schedule.class))).thenReturn(Schedule.builder().id(id).build());

    final UUID uuid =
        this.service
            .add(
                Scheduler.Schedule.newBuilder()
                    .setRecipient(recipientString)
                    .setType(Scheduler.Schedule.SendType.EMAIL)
                    .build())
            .get();

    assertNotNull(uuid);
    assertEquals(uuid, id);
    verify(this.repository, Mockito.times(1)).save(any(Schedule.class));
  }

  @Test
  void shouldThrowExceptionIfSendTypeNotExists() {
    when(this.recipientService.addRecipient(recipientString))
        .thenReturn(Optional.of(Recipient.builder().recipient(recipientString).build()));

    final Scheduler.Schedule dto =
        Scheduler.Schedule.newBuilder()
            .setRecipient(recipientString)
            .setType(Scheduler.Schedule.SendType.UNKNOWN)
            .build();

    assertThrows(ScheduleTypeException.class, () -> this.service.add(dto));
  }

  @Test
  void shouldGetScheduleById() {
    when(this.repository.findById(any(UUID.class)))
        .thenReturn(
            Optional.of(
                Schedule.builder()
                    .id(this.id)
                    .sendDate(Calendar.getInstance())
                    .message("it is a test")
                    .recipient(Recipient.builder().recipient(this.recipientString).build())
                    .sendType(SendType.WHATSAPP)
                    .scheduleStatus(ScheduleStatus.SCHEDULED)
                    .build()));

    final Response response = this.service.getScheduleById(this.id.toString()).get();

    verify(this.repository, times(1)).findById(this.id);
    assertNotNull(response);
    assertEquals(this.recipientString, response.getSchedule().getRecipient());
    assertEquals(SendType.WHATSAPP.name(), response.getSchedule().getType().name());
    assertEquals(ScheduleStatus.SCHEDULED.name(), response.getStatus());
  }

  @Test
  void shouldDeleteScheduleById() {
    final Schedule entity =
        Schedule.builder()
            .id(this.id)
            .sendDate(Calendar.getInstance())
            .message("it is a test")
            .recipient(Recipient.builder().recipient(this.recipientString).build())
            .sendType(SendType.WHATSAPP)
            .scheduleStatus(ScheduleStatus.SCHEDULED)
            .build();

    when(this.repository.findByIdAndScheduleStatus(any(UUID.class), any(ScheduleStatus.class)))
        .thenReturn(Optional.of(entity));

    when(this.repository.save(entity)).thenReturn(entity);

    final Response response = this.service.deleteScheduleById(this.id.toString()).get();

    final ArgumentCaptor<Schedule> captor = ArgumentCaptor.forClass(Schedule.class);

    verify(this.repository, times(1)).findByIdAndScheduleStatus(this.id, ScheduleStatus.SCHEDULED);
    verify(this.repository, times(1)).save(captor.capture());
    assertEquals(ScheduleStatus.EXCLUDED, captor.getValue().getScheduleStatus());
    assertNotNull(response);
    assertEquals(this.recipientString, response.getSchedule().getRecipient());
    assertEquals(SendType.WHATSAPP.name(), response.getSchedule().getType().name());
    assertEquals(ScheduleStatus.EXCLUDED.name(), response.getStatus());
  }

  @Test
  void shouldNeverCallDeletMethodFromRepositoryOnDeleteScheduleById() {
    final Schedule entity =
        Schedule.builder()
            .id(this.id)
            .sendDate(Calendar.getInstance())
            .message("it is a test")
            .recipient(Recipient.builder().recipient(this.recipientString).build())
            .sendType(SendType.WHATSAPP)
            .scheduleStatus(ScheduleStatus.SCHEDULED)
            .build();

    when(this.repository.findByIdAndScheduleStatus(any(UUID.class), any(ScheduleStatus.class)))
        .thenReturn(Optional.of(entity));

    when(this.repository.save(entity)).thenReturn(entity);

    this.service.deleteScheduleById(this.id.toString()).get();

    verify(this.repository, never()).delete(any(Schedule.class));
    verify(this.repository, never()).deleteById(any(UUID.class));
  }
}
