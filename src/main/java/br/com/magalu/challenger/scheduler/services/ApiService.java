package br.com.magalu.challenger.scheduler.services;

import java.net.URI;
import java.util.Optional;
import org.springframework.stereotype.Service;

public interface ApiService<T> {
  Optional<URI> add(T dto);
}
