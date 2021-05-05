package at.serviceengineering.microservice.user.controller

import com.google.gson.Gson
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class Consumer {
    @RabbitListener(queues = ["tut.rpc.requests"])
    fun receive(obj: String): String {

        println("Received: '$obj'")
        return obj + "recieved"
    }
}
