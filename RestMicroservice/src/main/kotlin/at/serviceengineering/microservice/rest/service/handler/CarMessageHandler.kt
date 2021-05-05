package at.serviceengineering.microservice.rest.service.handler

import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
@RabbitListener(queues = ["hello"])
class CarMessageHandler {
    @RabbitHandler
    fun receive(obj: String) {

        println("Received: '$obj'")
    }
}