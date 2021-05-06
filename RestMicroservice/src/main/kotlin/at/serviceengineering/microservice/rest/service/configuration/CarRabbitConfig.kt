package at.serviceengineering.microservice.rest.service.configuration

import org.springframework.amqp.core.DirectExchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CarRabbitConfig {

    @Bean
    fun exchangeRestGetCars(): DirectExchange? {
        return DirectExchange("rest.getCars")
    }

    @Bean
    fun exchangeRestEditCar(): DirectExchange? {
        return DirectExchange("rest.editCar")
    }
}
