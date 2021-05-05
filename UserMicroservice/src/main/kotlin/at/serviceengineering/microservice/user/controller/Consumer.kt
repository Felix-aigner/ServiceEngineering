package at.serviceengineering.microservice.user.controller

import com.google.gson.Gson
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
@RabbitListener(queues = ["hello"])
class Consumer (
        val producer: Producer
        ) {
    @RabbitHandler
    fun receive(obj: String) {

        println("Received: '$obj'")
        producer.send()
    }
}
