package br.com.magalu.challenger.scheduler.applications.validations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class EmailTypeValidationTests {

  private final EmailTypeValidations emailTypeValidation = new EmailTypeValidations();

  @Test
  void shouldValidateEmail() {
    assertTrue(this.emailTypeValidation.validate("email.teste@email.com"));
  }

  @Test
  void shouldInvalidateEmailThatIsInvalid() {
    assertFalse(this.emailTypeValidation.validate("email.teste"));
  }

  @Test
  void shouldInvalidateEmailThatIsIncomplete() {
    assertFalse(this.emailTypeValidation.validate("email.teste@email"));
  }
}
