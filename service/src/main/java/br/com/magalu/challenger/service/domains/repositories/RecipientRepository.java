package br.com.magalu.challenger.service.domains.repositories;

import br.com.magalu.challenger.service.domains.entities.Recipient;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipientRepository extends JpaRepository<Recipient, UUID> {

  Optional<Recipient> findByRecipient(String recipient);
}
