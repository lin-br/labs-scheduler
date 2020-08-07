package br.com.magalu.challenger.scheduler.services;

import br.com.magalu.challenger.scheduler.domains.entities.Recipient;
import br.com.magalu.challenger.scheduler.domains.repositories.RecipientRepository;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(propagation = Propagation.MANDATORY)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class RecipientService {

  private final RecipientRepository recipientRepository;

  public Optional<Recipient> addRecipient(String recipient) {
    return this.recipientRepository
        .findByRecipient(recipient)
        .or(() -> this.saveRecipient(recipient));
  }

  private Optional<Recipient> saveRecipient(String recipient) {
    return Optional.of(
        this.recipientRepository.save(Recipient.builder().recipient(recipient).build()));
  }
}
