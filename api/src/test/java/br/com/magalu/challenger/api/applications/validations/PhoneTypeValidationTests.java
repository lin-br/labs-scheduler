package br.com.magalu.challenger.api.applications.validations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PhoneTypeValidationTests {

  private final PhoneTypeValidation phoneTypeValidation = new PhoneTypeValidation();

  @Test
  void shouldInvalidatePhoneNumberThatIsContainsLetters() {
    assertFalse(this.phoneTypeValidation.validate("a16976269824"));
  }

  @Test
  void shouldInvalidatePhoneNumberThatIsSmall() {
    assertFalse(this.phoneTypeValidation.validate("312"));
  }

  @Test
  void shouldValidatePhoneNumber() {
    assertTrue(this.phoneTypeValidation.validate("16999999312"));
  }
}
