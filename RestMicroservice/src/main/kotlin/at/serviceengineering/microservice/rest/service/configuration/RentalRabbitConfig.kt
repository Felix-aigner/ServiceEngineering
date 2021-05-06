package at.serviceengineering.microservice.rest.service.configuration

import org.springframework.amqp.core.DirectExchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RentalRabbitConfig {

    @Bean
    fun exchangeRestGetRentals(): DirectExchange? {
        return DirectExchange("rest.getRentals")
    }

    @Bean
    fun exchangeRestRequestRental(): DirectExchange? {
        return DirectExchange("rest.requestRental")
    }
}
