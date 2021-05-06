package at.serviceengineering.microservice.rental.configuration

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RestRabbitConfig {

    @Bean
    fun getRentalsQueue(): Queue? {
        return Queue("rentals.getRentals.requests")
    }

    @Bean
    fun exchangeRestGetRentals(): DirectExchange? {
        return DirectExchange("rest.getRentals")
    }

    @Bean
    fun bindingRestGetRentals(exchangeRestGetRentals: DirectExchange, getRentalsQueue: Queue): Binding? {
        return BindingBuilder.bind(getRentalsQueue)
                .to(exchangeRestGetRentals)
                .with("rentals.getRentals")
    }


    @Bean
    fun requestRentalQueue(): Queue? {
        return Queue("rentals.requestRental.requests")
    }

    @Bean
    fun exchangeRestRequestRental(): DirectExchange? {
        return DirectExchange("rest.requestRental")
    }

    @Bean
    fun bindingRestEditCar(exchangeRestRequestRental: DirectExchange, requestRentalQueue: Queue): Binding? {
        return BindingBuilder.bind(requestRentalQueue)
                .to(exchangeRestRequestRental)
                .with("rentals.requestRental")
    }
}
