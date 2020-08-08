package br.com.magalu.challenger.scheduler.services;

import java.util.Optional;
import java.util.UUID;

public interface ApiService<T> {

  Optional<UUID> add(T dto);

  Optional<T> getScheduleById(String id);

  Optional<T> deleteScheduleById(String id);
}
