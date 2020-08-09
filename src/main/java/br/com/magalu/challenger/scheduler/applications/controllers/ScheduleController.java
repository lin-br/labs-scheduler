package br.com.magalu.challenger.scheduler.applications.controllers;

import br.com.magalu.challenger.scheduler.applications.dtos.ScheduleDto;
import br.com.magalu.challenger.scheduler.applications.dtos.errors.MessageBodyResponseDto;
import br.com.magalu.challenger.scheduler.applications.validations.TypeValidationsFactory;
import br.com.magalu.challenger.scheduler.services.ApiService;
import java.io.Serializable;
import java.net.URI;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(ScheduleController.PATH)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class ScheduleController {

  static final String PATH = "/schedules";
  static final String PATH_GET_SCHEDULE_BY_ID = "/{id}";

  private final ApiService<ScheduleDto> service;

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Serializable> addSchedule(
      @Validated @RequestBody final ScheduleDto payload,
      UriComponentsBuilder uriComponentsBuilder) {
    if (TypeValidationsFactory.of(payload.getType()).validate(payload.getRecipient())) {
      return this.service
          .add(payload)
          .map(uuid -> this.getUriToStatusCode200(uriComponentsBuilder, uuid))
          .map(this::getResponseOfStatusCreated)
          .orElseGet(this::getResponseOfStatusUnprocessableEntity);
    } else {
      return this.getResponseBadRequest(payload);
    }
  }

  private ResponseEntity<Serializable> getResponseBadRequest(ScheduleDto payload) {
    return ResponseEntity.badRequest()
        .body(
            MessageBodyResponseDto.builder()
                .message(
                    String.format(
                        "Encontrado incompatibilidade entre o tipo de envio [%s] e o destinatário [%s].",
                        payload.getType(), payload.getRecipient()))
                .build());
  }

  @GetMapping(value = PATH_GET_SCHEDULE_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ScheduleDto> getScheduleById(@PathVariable final String id) {
    return this.service
        .getScheduleById(id)
        .map(this::getOkResponse)
        .orElseGet(this::getNotFoundResponse);
  }

  @DeleteMapping(value = PATH_GET_SCHEDULE_BY_ID)
  public ResponseEntity<ScheduleDto> deleteScheduleById(@PathVariable final String id) {
    return this.service
        .deleteScheduleById(id)
        .map(this::getOkResponse)
        .orElseGet(this::getNotFoundResponse);
  }

  private ResponseEntity<ScheduleDto> getNotFoundResponse() {
    return ResponseEntity.notFound().build();
  }

  private ResponseEntity<ScheduleDto> getOkResponse(ScheduleDto dto) {
    return ResponseEntity.ok(dto);
  }

  private ResponseEntity<Serializable> getResponseOfStatusCreated(final URI uri) {
    return ResponseEntity.created(uri).build();
  }

  private ResponseEntity<Serializable> getResponseOfStatusUnprocessableEntity() {
    return ResponseEntity.unprocessableEntity().build();
  }

  private URI getUriToStatusCode200(UriComponentsBuilder uriComponentsBuilder, final UUID id) {
    return uriComponentsBuilder
        .path(PATH.concat(PATH_GET_SCHEDULE_BY_ID))
        .buildAndExpand(id)
        .toUri();
  }
}
