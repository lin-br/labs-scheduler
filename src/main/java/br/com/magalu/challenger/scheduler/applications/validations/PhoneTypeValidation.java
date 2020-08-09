package br.com.magalu.challenger.scheduler.applications.validations;

import java.util.regex.Pattern;

class PhoneTypeValidation implements TypeValidations {

  @Override
  public boolean validate(String payload) {
    return Pattern.matches("\\d+", payload) && payload.length() >= 9;
  }
}
