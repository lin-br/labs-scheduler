package br.com.magalu.challenger.scheduler.applications.controllers;

import br.com.magalu.challenger.scheduler.applications.dtos.ScheduleDto;
import br.com.magalu.challenger.scheduler.applications.dtos.SendTypeDto;
import br.com.magalu.challenger.scheduler.applications.dtos.errors.MessageBodyResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Calendar;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(ScheduleControllerIntegrationTests.PROFILE_TEST)
public class ScheduleControllerIntegrationTests {

  static final String PROFILE_TEST = "test";
  private final ResultMatcher statusResultMatcher = MockMvcResultMatchers.status().isBadRequest();
  @Autowired
  private ObjectMapper mapper;
  @Autowired
  private MockMvc mockMvc;

  @Test
  void shoulGetResponseEntityWithScheduleTypeErrorMessage() throws Exception {
    final ScheduleDto dto =
        ScheduleDto.builder()
            .type(null)
            .recipient("test")
            .message("it is a test")
            .date(Calendar.getInstance())
            .build();

    final MessageBodyResponseDto bodyDto =
        MessageBodyResponseDto.builder().message("O tipo do envio está inválido").build();

    final MockHttpServletRequestBuilder request =
        MockMvcRequestBuilders.post(ScheduleController.PATH)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(this.mapper.writeValueAsString(dto));

    final ResultMatcher bodyResultMatcher =
        MockMvcResultMatchers.content().json(this.mapper.writeValueAsString(bodyDto));

    this.mockMvc.perform(request).andExpect(this.statusResultMatcher).andExpect(bodyResultMatcher);
  }

  @Test
  void shoulGetResponseEntityWithoutTheRecipient() throws Exception {
    final ScheduleDto dto =
        ScheduleDto.builder()
            .type(SendTypeDto.PUSH)
            .message("it is a test")
            .date(Calendar.getInstance())
            .build();

    final MessageBodyResponseDto bodyDto =
        MessageBodyResponseDto.builder()
            .message("Encontrado erro [must not be null] no campo: recipient")
            .build();

    final MockHttpServletRequestBuilder request =
        MockMvcRequestBuilders.post(ScheduleController.PATH)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(this.mapper.writeValueAsString(dto));

    final ResultMatcher bodyResultMatcher =
        MockMvcResultMatchers.content().json(this.mapper.writeValueAsString(bodyDto));

    this.mockMvc.perform(request).andExpect(this.statusResultMatcher).andExpect(bodyResultMatcher);
  }
}
