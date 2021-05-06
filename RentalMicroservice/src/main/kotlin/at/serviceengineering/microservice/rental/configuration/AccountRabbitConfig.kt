package at.serviceengineering.microservice.rental.configuration

import org.springframework.amqp.core.DirectExchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class AccountRabbitConfig {

    @Bean
    fun exchangeRentalIsAccount(): DirectExchange? {
        return DirectExchange("rental.isAccount")
    }
}
