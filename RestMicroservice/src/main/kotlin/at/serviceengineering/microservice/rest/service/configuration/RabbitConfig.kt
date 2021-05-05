package at.serviceengineering.microservice.rest.service.configuration

import org.springframework.amqp.core.DirectExchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RabbitConfig {

    @Bean
    fun exchange(): DirectExchange? {
        return DirectExchange("tut.rpc")
    }

}
