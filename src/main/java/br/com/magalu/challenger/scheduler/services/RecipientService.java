package br.com.magalu.challenger.scheduler.services;

import br.com.magalu.challenger.scheduler.domains.entities.Recipient;
import br.com.magalu.challenger.scheduler.domains.repositories.RecipientRepository;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class RecipientService {
  private final RecipientRepository recipientRepository;

  public Optional<Recipient> addRecipient(String recipient) {
    return Optional.of(
        this.recipientRepository.save(Recipient.builder().recipient(recipient).build()));
  }
}
