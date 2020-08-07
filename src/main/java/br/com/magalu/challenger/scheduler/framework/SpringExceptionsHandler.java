package br.com.magalu.challenger.scheduler.framework;

import br.com.magalu.challenger.scheduler.applications.dtos.errors.MessageBodyResponseDto;
import br.com.magalu.challenger.scheduler.services.exceptions.ScheduleTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class SpringExceptionsHandler {

  @ExceptionHandler(value = ScheduleTypeException.class)
  ResponseEntity<MessageBodyResponseDto> handlerScheduleTypeException(
      ScheduleTypeException exception) {
    return ResponseEntity.badRequest()
        .body(MessageBodyResponseDto.builder().message(exception.getMessage()).build());
  }
}
