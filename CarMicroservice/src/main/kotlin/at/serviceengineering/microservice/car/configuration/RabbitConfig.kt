package at.serviceengineering.microservice.car.configuration

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RabbitConfig {

    @Bean
    fun getCarsQueue(): Queue? {
        return Queue("cars.getCars.requests")
    }

    @Bean
    fun exchangeRestGetCars(): DirectExchange? {
        return DirectExchange("rest.getCars")
    }

    @Bean
    fun bindingRestGetCars(exchangeRestGetCars: DirectExchange , getCarsQueue: Queue): Binding? {
        return BindingBuilder.bind(getCarsQueue)
                .to(exchangeRestGetCars)
                .with("cars.getCars")
    }

    @Bean
    fun exchangeRentalGetCars(): DirectExchange? {
        return DirectExchange("rest.getCars")
    }

    @Bean
    fun bindingRentalGetCars(exchangeRentalGetCars: DirectExchange , getCarsQueue: Queue): Binding? {
        return BindingBuilder.bind(getCarsQueue)
                .to(exchangeRentalGetCars)
                .with("cars.getCars")
    }




    @Bean
    fun editCarQueue(): Queue? {
        return Queue("cars.editCar.requests")
    }

    @Bean
    fun exchangeRestEditCar(): DirectExchange? {
        return DirectExchange("rest.editCar")
    }

    @Bean
    fun bindingRestEditCar(exchangeRestEditCar: DirectExchange , editCarQueue: Queue): Binding? {
        return BindingBuilder.bind(editCarQueue)
                .to(exchangeRestEditCar)
                .with("cars.editCar")
    }
}
