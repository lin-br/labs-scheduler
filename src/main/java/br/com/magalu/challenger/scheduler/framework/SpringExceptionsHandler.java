package br.com.magalu.challenger.scheduler.framework;

import br.com.magalu.challenger.scheduler.applications.dtos.errors.MessageBodyResponseDto;
import br.com.magalu.challenger.scheduler.services.exceptions.ScheduleTypeException;
import java.util.Objects;
import java.util.Optional;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  ResponseEntity<MessageBodyResponseDto> handlerMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    final Optional<FieldError> type =
        ex.getBindingResult().getFieldErrors().stream()
            .filter(field -> field.getField().equals("type"))
            .findFirst();

    if (type.isPresent()) {
      return ResponseEntity.badRequest()
          .body(MessageBodyResponseDto.builder().message("O tipo do envio está inválido").build());
    } else {
      final ObjectError objectError = ex.getBindingResult().getAllErrors().get(0);
      final DefaultMessageSourceResolvable argument =
          (DefaultMessageSourceResolvable) Objects.requireNonNull(objectError.getArguments())[0];
      return ResponseEntity.badRequest()
          .body(
              MessageBodyResponseDto.builder()
                  .message(
                      String.format(
                          "Encontrado erro [%s] no campo: %s",
                          objectError.getDefaultMessage(), argument.getDefaultMessage()))
                  .build());
    }
  }
}
