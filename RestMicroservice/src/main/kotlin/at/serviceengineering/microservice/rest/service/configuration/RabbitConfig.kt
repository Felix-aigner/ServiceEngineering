package at.serviceengineering.microservice.rest.service.configuration

import org.springframework.amqp.core.DirectExchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RabbitConfig {

    @Bean
    fun nameExchange(): DirectExchange? {
        return DirectExchange("tut.rpc")
    }

    @Bean
    fun accountsExchange(): DirectExchange? {
        return DirectExchange("rest.getAccounts")
    }

    @Bean
    fun accountCreationExchange(): DirectExchange? {
        return DirectExchange("rest.createAccount")
    }

    @Bean
    fun loginExchange(): DirectExchange? {
        return DirectExchange("rest.login")
    }

    @Bean
    fun deleteExchange(): DirectExchange? {
        return DirectExchange("rest.deleteAccount")
    }

}
