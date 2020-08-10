package br.com.magalu.challenger.api.applications.validations;

import org.apache.commons.validator.routines.EmailValidator;

class EmailTypeValidations implements TypeValidations {

  @Override
  public boolean validate(String payload) {
    return EmailValidator.getInstance().isValid(payload);
  }
}
