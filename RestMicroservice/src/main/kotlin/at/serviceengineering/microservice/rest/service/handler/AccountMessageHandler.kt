package at.serviceengineering.microservice.rest.service.handler

import com.google.gson.Gson
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class AccountMessageHandler (
        val rabbitTemplate: RabbitTemplate
        ){

    @RabbitListener(queues = ["hello2"])
    fun receive(obj: String) : String{
        println("Received: '$obj'")
        return obj
    }

    fun sendTestMessage(name: String) {
        rabbitTemplate.convertSendAndReceive("hello", Gson().toJson(name))
    }
}