package br.com.magalu.challenger.scheduler.applications.validations;

import br.com.magalu.challenger.scheduler.applications.dtos.SendTypeDto;

public final class TypeValidationsFactory {

  private TypeValidationsFactory() {
  }

  public static TypeValidations of(final SendTypeDto typeDto) {
    switch (typeDto) {
      case SMS:
      case WHATSAPP:
        return new PhoneTypeValidation();
      case EMAIL:
        return new EmailTypeValidations();
      default:
        return new DefaultTypeValidation();
    }
  }
}
