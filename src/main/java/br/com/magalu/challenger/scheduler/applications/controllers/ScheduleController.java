package br.com.magalu.challenger.scheduler.applications.controllers;

import br.com.magalu.challenger.scheduler.applications.dtos.ScheduleDto;
import br.com.magalu.challenger.scheduler.services.ApiService;
import java.io.Serializable;
import java.net.URI;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ScheduleController.PATH)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class ScheduleController {

  static final String PATH = "/schedules";
  private final ApiService<ScheduleDto> service;

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Serializable> addScheduler(
      @Validated @RequestBody final ScheduleDto payload) {
    return this.service
        .add(payload)
        .map(this::getResponseOfStatusCreated)
        .orElseGet(this::getResponseOfStatusUnprocessableEntity);
  }

  private ResponseEntity<Serializable> getResponseOfStatusCreated(final URI uri) {
    return ResponseEntity.created(uri).build();
  }

  private ResponseEntity<Serializable> getResponseOfStatusUnprocessableEntity() {
    return ResponseEntity.unprocessableEntity().build();
  }
}
