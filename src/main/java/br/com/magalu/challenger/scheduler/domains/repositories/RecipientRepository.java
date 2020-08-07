package br.com.magalu.challenger.scheduler.domains.repositories;

import br.com.magalu.challenger.scheduler.domains.entities.Recipient;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipientRepository extends JpaRepository<Recipient, UUID> {}
