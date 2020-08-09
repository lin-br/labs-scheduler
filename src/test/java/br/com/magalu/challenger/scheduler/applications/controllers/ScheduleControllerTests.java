package br.com.magalu.challenger.scheduler.applications.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import br.com.magalu.challenger.scheduler.applications.dtos.ScheduleDto;
import br.com.magalu.challenger.scheduler.applications.dtos.SendTypeDto;
import br.com.magalu.challenger.scheduler.domains.entities.ScheduleStatus;
import br.com.magalu.challenger.scheduler.services.ApiService;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

public class ScheduleControllerTests {

  private final UUID id = UUID.fromString("df368c62-9054-4b39-ab7a-46c172d6fc58");
  private ScheduleController controller;
  private UriComponentsBuilder uriComponentsBuilder;
  private ScheduleDto dto;
  @Spy
  private ApiService<ScheduleDto> service;

  @BeforeEach
  void setup() {
    MockitoAnnotations.initMocks(this);
    this.controller = new ScheduleController(this.service);

    this.uriComponentsBuilder =
        UriComponentsBuilder.newInstance()
            .scheme("http")
            .host("localhost")
            .port(8080)
            .path(ScheduleController.PATH);

    this.dto =
        ScheduleDto.builder()
            .type(SendTypeDto.PUSH)
            .recipient("test")
            .message("it is a test")
            .date(Calendar.getInstance())
            .build();
  }

  @Test
  void shouldAddScheduleAndGetTheResponseEntityWithStatusCode201AndLocationHeader() {
    when(this.service.add(any(ScheduleDto.class))).thenReturn(Optional.of(id));

    final ResponseEntity<Serializable> response =
        this.controller.addSchedule(this.dto, this.uriComponentsBuilder);

    verify(this.service, times(1)).add(this.dto);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertTrue(response.getHeaders().containsKey("Location"));
  }

  @Test
  void shouldAddScheduleAndGetTheResponseEntityWithStatusCode422() {
    when(this.service.add(any(ScheduleDto.class))).thenReturn(Optional.empty());

    final ResponseEntity<Serializable> response =
        this.controller.addSchedule(this.dto, this.uriComponentsBuilder);

    verify(this.service, times(1)).add(dto);
    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
  }

  @Test
  void shouldGetScheduleByIdAndGetTheResponseEntityWithStatusCode200AndScheduleInsideOfBody() {
    when(this.service.getScheduleById(anyString())).thenReturn(Optional.of(this.dto));

    final ResponseEntity<ScheduleDto> response =
        this.controller.getScheduleById(this.id.toString());

    verify(this.service, times(1)).getScheduleById(this.id.toString());
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(this.dto, response.getBody());
  }

  @Test
  void shouldGetScheduleByIdAndGetTheResponseEntityWithStatusCode404() {
    when(this.service.getScheduleById(anyString())).thenReturn(Optional.empty());

    final ResponseEntity<ScheduleDto> response =
        this.controller.getScheduleById(this.id.toString());

    verify(this.service, times(1)).getScheduleById(this.id.toString());
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void shouldDeleteScheduleByIdAndGetTheResponseEntityWithStatusCode200AndScheduleInsideOfBody() {
    final ScheduleDto dto = ScheduleDto.builder().status(ScheduleStatus.EXCLUDED.name()).build();

    when(this.service.deleteScheduleById(anyString())).thenReturn(Optional.of(dto));

    final ResponseEntity<ScheduleDto> response =
        this.controller.deleteScheduleById(this.id.toString());

    verify(this.service, times(1)).deleteScheduleById(this.id.toString());
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(dto, response.getBody());
    assertEquals(
        ScheduleStatus.EXCLUDED.name(), Objects.requireNonNull(response.getBody()).getStatus());
  }

  @Test
  void shouldDeleteScheduleByIdAndGetTheResponseEntityWithStatusCode404() {
    when(this.service.deleteScheduleById(anyString())).thenReturn(Optional.empty());

    final ResponseEntity<ScheduleDto> response =
        this.controller.deleteScheduleById(this.id.toString());

    verify(this.service, times(1)).deleteScheduleById(this.id.toString());
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void shouldGetBadRequestIfTheRecipientNotMatchWithSendType() {
    ScheduleDto dto =
        ScheduleDto.builder()
            .type(SendTypeDto.EMAIL)
            .recipient("it is not an email")
            .message("it is a test")
            .date(Calendar.getInstance())
            .build();

    final ResponseEntity<Serializable> response =
        this.controller.addSchedule(dto, this.uriComponentsBuilder);

    verifyNoInteractions(this.service);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
}
