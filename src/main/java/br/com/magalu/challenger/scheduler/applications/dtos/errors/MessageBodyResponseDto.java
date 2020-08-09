package br.com.magalu.challenger.scheduler.applications.dtos.errors;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MessageBodyResponseDto implements Serializable {

  @NotNull
  @NotBlank
  private String message;
}
