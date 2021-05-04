package at.serviceengineering.webservice1.services

import at.serviceengineering.webservice1.dtos.LoginDto
import com.google.gson.Gson
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
@RabbitListener(queues = ["hello"])
class RabbitListenerService {
    @RabbitHandler
    fun receive(obj: String) {

        println("Received: '${Gson().fromJson(obj, LoginDto::class.java).username}'")
    }
}
