package at.serviceengineering.microservice.user.configuration

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RabbitConfig {

    //----------getAccounts------------------

    @Bean
    fun getAccountsQueue(): Queue? {
        return Queue("user.getAccounts.requests")
    }

    @Bean
    fun exchangeRestGetAccounts(): DirectExchange? {
        return DirectExchange("rest.getAccounts")
    }

    @Bean
    fun bindingRestGetAccounts(exchangeRestGetAccounts: DirectExchange , getAccountsQueue: Queue): Binding? {
        return BindingBuilder.bind(getAccountsQueue)
                .to(exchangeRestGetAccounts)
                .with("user.getAccounts")
    }

    //----------getNameREST------------------

    @Bean
    fun getNameQueue(): Queue? {
        return Queue("user.getName.requests")
    }

    @Bean
    fun exchangeRestGetName(): DirectExchange? {
        return DirectExchange("rest.getName")
    }

    @Bean
    fun bindingRestGetName(exchangeRestGetName: DirectExchange , getNameQueue: Queue): Binding? {
        return BindingBuilder.bind(getNameQueue)
                .to(exchangeRestGetName)
                .with("user.getName")
    }

    //----------getNameRentals------------------

    @Bean
    fun exchangeRentalsGetName(): DirectExchange? {
        return DirectExchange("rentals.getName")
    }

    @Bean
    fun bindingRentalGetName(exchangeRentalsGetName: DirectExchange , getNameQueue: Queue): Binding? {
        return BindingBuilder.bind(getNameQueue)
                .to(exchangeRentalsGetName)
                .with("user.getName")
    }

    //----------createAccountRest------------------

    @Bean
    fun createAccountQueue(): Queue? {
        return Queue("user.createAccount.requests")
    }

    @Bean
    fun exchangeRestCreateAccount(): DirectExchange? {
        return DirectExchange("rest.createAccount")
    }

    @Bean
    fun bindingRestCreateAccount(exchangeRestCreateAccount: DirectExchange , createAccountQueue: Queue): Binding? {
        return BindingBuilder.bind(createAccountQueue)
                .to(exchangeRestCreateAccount)
                .with("user.createAccount")
    }

    //----------loginRest------------------

    @Bean
    fun loginQueue(): Queue? {
        return Queue("user.login.requests")
    }

    @Bean
    fun exchangeRestLogin(): DirectExchange? {
        return DirectExchange("rest.login")
    }

    @Bean
    fun bindingRestLogin(exchangeRestLogin: DirectExchange , loginQueue: Queue): Binding? {
        return BindingBuilder.bind(loginQueue)
                .to(exchangeRestLogin)
                .with("user.login")
    }

    //----------deleteAccountRest------------------

    @Bean
    fun deleteAccountQueue(): Queue? {
        return Queue("user.deleteAccount.requests")
    }

    @Bean
    fun exchangeRestDeleteAccount(): DirectExchange? {
        return DirectExchange("rest.deleteAccount")
    }

    @Bean
    fun bindingRestDeleteAccount(exchangeRestDeleteAccount: DirectExchange , deleteAccountQueue: Queue): Binding? {
        return BindingBuilder.bind(deleteAccountQueue)
                .to(exchangeRestDeleteAccount)
                .with("user.deleteAccount")
    }

}
