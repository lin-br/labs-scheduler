package br.com.magalu.challenger.api.services;

import com.google.protobuf.GeneratedMessageV3;
import java.util.Optional;

public interface BrokerService<T extends GeneratedMessageV3, R extends GeneratedMessageV3> {

  Optional<R> sendMessageAndReceive(String bindingKey, T t);
}
