package br.com.magalu.challenger.service.domains.repositories;

import br.com.magalu.challenger.service.domains.entities.Schedule;
import br.com.magalu.challenger.service.domains.entities.ScheduleStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {

  Optional<Schedule> findByIdAndScheduleStatus(UUID id, ScheduleStatus status);
}
