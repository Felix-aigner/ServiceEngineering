package at.serviceengineering.microservice.user.configuration

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RabbitConfig {

    @Bean
    fun queue(): Queue? {
        return Queue("user.getName.requests")
    }

    @Bean
    fun exchangeRestGetName(): DirectExchange? {
        return DirectExchange("rest.getName")
    }

    @Bean
    fun exchangeRentalsGetName(): DirectExchange? {
        return DirectExchange("rentals.getName")
    }

    @Bean
    fun bindingRestGetName(exchangeRestGetName: DirectExchange , queue: Queue): Binding? {
        return BindingBuilder.bind(queue)
                .to(exchangeRestGetName)
                .with("user.getName")
    }

    @Bean
    fun bindingRentalGetName(exchangeRentalsGetName: DirectExchange , queue: Queue): Binding? {
        return BindingBuilder.bind(queue)
                .to(exchangeRentalsGetName)
                .with("user.getName")
    }
}
