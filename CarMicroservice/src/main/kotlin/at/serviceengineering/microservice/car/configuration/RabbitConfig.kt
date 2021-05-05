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
    fun addCarQueue(): Queue? {
        return Queue("cars.addCar.requests")
    }

    @Bean
    fun exchangeRestAddCar(): DirectExchange? {
        return DirectExchange("rest.addCar")
    }

    @Bean
    fun bindingRestAddCar(exchangeRestAddCar: DirectExchange , addCarQueue: Queue): Binding? {
        return BindingBuilder.bind(addCarQueue)
                .to(exchangeRestAddCar)
                .with("cars.addCar")
    }
}
