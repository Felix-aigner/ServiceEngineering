package at.serviceengineering.microservice.car.controller

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class Producer(
        val rabbitTemplate: RabbitTemplate
) {
    fun send() {
        rabbitTemplate.convertAndSend("hello", "test")
    }
}
