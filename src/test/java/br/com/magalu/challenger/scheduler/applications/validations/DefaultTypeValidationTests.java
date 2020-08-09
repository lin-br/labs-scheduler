package br.com.magalu.challenger.scheduler.applications.validations;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DefaultTypeValidationTests {

  private final DefaultTypeValidation emailTypeValidation = new DefaultTypeValidation();

  @Test
  void shouldReturnTrueForever() {
    assertTrue(this.emailTypeValidation.validate("it is true"));
  }
}
