package at.serviceengineering.microservice.user.controller

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class Consumer {
    @RabbitListener(queues = ["user.getName.requests"])
    fun receive(obj: String): String {

        println("Received: '$obj'")
        return "Hans"
    }
}
