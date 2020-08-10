package br.com.magalu.challenger.service;

import br.com.magalu.challenger.commons.applications.SchedulerProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ServiceApplication.class, args);
  }

  @Bean(SchedulerProperties.QUEUE_ADD_SCHEDULE)
  public Queue getQueueToAdd() {
    return new Queue(SchedulerProperties.QUEUE_ADD_SCHEDULE);
  }

  @Bean(SchedulerProperties.QUEUE_GET_SCHEDULE)
  public Queue getQueueToGet() {
    return new Queue(SchedulerProperties.QUEUE_GET_SCHEDULE);
  }

  @Bean(SchedulerProperties.QUEUE_DEL_SCHEDULE)
  public Queue getQueueToDel() {
    return new Queue(SchedulerProperties.QUEUE_DEL_SCHEDULE);
  }

  @Bean
  public DirectExchange getExchange() {
    return new DirectExchange(SchedulerProperties.EXCHANGE_RPC_SCHEDULE);
  }

  @Bean
  public Binding getBindingToAdd(DirectExchange exchange) {
    return BindingBuilder.bind(getQueueToAdd()).to(exchange).with(getQueueToAdd().getName());
  }

  @Bean
  public Binding getBindingToGet(DirectExchange exchange) {
    return BindingBuilder.bind(getQueueToGet()).to(exchange).with(getQueueToGet().getName());
  }

  @Bean
  public Binding getBindingToDel(DirectExchange exchange) {
    return BindingBuilder.bind(getQueueToDel()).to(exchange).with(getQueueToDel().getName());
  }
}
