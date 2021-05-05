package at.serviceengineering.microservice.rental.controller

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class Producer(
        val rabbitTemplate: RabbitTemplate
) {
    fun send() {
        val response = rabbitTemplate.convertSendAndReceive("rest.getName", "user.getName", "rental") as String?
        println(" [.] Got '$response'")
    }
}
