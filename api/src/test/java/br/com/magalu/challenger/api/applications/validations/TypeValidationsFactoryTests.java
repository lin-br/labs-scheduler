package br.com.magalu.challenger.api.applications.validations;

import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.magalu.challenger.api.applications.dtos.SendTypeDto;
import org.junit.jupiter.api.Test;

class TypeValidationsFactoryTests {

  @Test
  void shouldGetPhoneTypeValidationWithSendTypeOfSms() {
    assertTrue(TypeValidationsFactory.of(SendTypeDto.SMS) instanceof PhoneTypeValidation);
  }

  @Test
  void shouldGetPhoneTypeValidationWithSendTypeOfWhatsapp() {
    assertTrue(TypeValidationsFactory.of(SendTypeDto.WHATSAPP) instanceof PhoneTypeValidation);
  }

  @Test
  void shouldGetEmailTypeValidationWithSendTypeOfEmail() {
    assertTrue(TypeValidationsFactory.of(SendTypeDto.EMAIL) instanceof EmailTypeValidations);
  }

  @Test
  void shouldGetDefaultTypeValidationIfSendTypeValidationNotExists() {
    assertTrue(TypeValidationsFactory.of(SendTypeDto.PUSH) instanceof DefaultTypeValidation);
  }
}
