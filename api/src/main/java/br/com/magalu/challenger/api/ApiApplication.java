package br.com.magalu.challenger.api;

import br.com.magalu.challenger.commons.applications.SchedulerProperties;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApiApplication.class, args);
  }

  @Bean
  public DirectExchange exchange() {
    return new DirectExchange(SchedulerProperties.EXCHANGE_RPC_SCHEDULE);
  }
}
