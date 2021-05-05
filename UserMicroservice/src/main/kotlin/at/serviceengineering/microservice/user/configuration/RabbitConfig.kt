package at.serviceengineering.microservice.user.configuration

import at.serviceengineering.microservice.user.controller.Consumer
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder

import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration;


@Configuration
class RabbitConfig {

    @Bean
    fun queue(): Queue? {
        return Queue("tut.rpc.requests")
    }

    @Bean
    fun exchange(): DirectExchange? {
        return DirectExchange("tut.rpc")
    }

    @Bean
    fun binding(exchange: DirectExchange?,
                queue: Queue?): Binding? {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with("rpc")
    }

    @Bean
    fun server(): Consumer? {
        return Consumer()
    }
}
