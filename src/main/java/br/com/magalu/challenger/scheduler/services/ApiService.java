package br.com.magalu.challenger.scheduler.services;

import java.net.URI;
import java.util.Optional;

public interface ApiService<T> {

  Optional<URI> add(T dto);

  Optional<T> getScheduleById(String id);
}
