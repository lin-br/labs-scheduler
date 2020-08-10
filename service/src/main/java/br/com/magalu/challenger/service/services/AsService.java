package br.com.magalu.challenger.service.services;

import com.google.protobuf.GeneratedMessageV3;
import java.util.Optional;
import java.util.UUID;

public interface AsService<T extends GeneratedMessageV3, R extends GeneratedMessageV3> {

  Optional<UUID> add(T t);

  Optional<R> getScheduleById(String id);

  Optional<R> deleteScheduleById(String id);
}
