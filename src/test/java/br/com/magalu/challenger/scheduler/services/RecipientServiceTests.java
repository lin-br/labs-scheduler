package br.com.magalu.challenger.scheduler.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.magalu.challenger.scheduler.domains.entities.Recipient;
import br.com.magalu.challenger.scheduler.domains.repositories.RecipientRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RecipientServiceTests {

  private RecipientService service;
  private RecipientRepository repository;

  @BeforeEach
  void setup() {
    this.repository = Mockito.spy(RecipientRepository.class);
    this.service = new RecipientService(this.repository);
  }

  @Test
  void shouldAddRecipientOnDatabase() {
    when(this.repository.findByRecipient(anyString())).thenReturn(Optional.empty());
    when(this.repository.save(any(Recipient.class))).thenReturn(Recipient.builder().build());

    this.service.addRecipient("test");

    verify(this.repository, times(1)).save(any(Recipient.class));
  }

  @Test
  void shouldGetRecipientFromDatabaseIfRecipientExists() {
    when(this.repository.findByRecipient(anyString()))
        .thenReturn(Optional.of(Recipient.builder().build()));

    this.service.addRecipient("test");

    verify(this.repository, never()).save(any(Recipient.class));
  }
}
