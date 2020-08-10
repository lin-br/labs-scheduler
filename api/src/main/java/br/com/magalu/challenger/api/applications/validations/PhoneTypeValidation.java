package br.com.magalu.challenger.api.applications.validations;

import java.util.regex.Pattern;

class PhoneTypeValidation implements TypeValidations {

  @Override
  public boolean validate(String payload) {
    return Pattern.matches("\\d+", payload) && payload.length() >= 9;
  }
}
