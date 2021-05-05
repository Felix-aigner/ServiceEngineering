package at.serviceengineering.microservice.rest.service.handler

import com.google.gson.Gson
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AccountMessageHandler {

    @Autowired
    private val template: RabbitTemplate? = null

    @Autowired
    private val exchange: DirectExchange? = null


    fun send(name: String): String {
        println(" [x] Requesting ($name)")
        val response = template!!.convertSendAndReceive(exchange!!.name, "rpc", name) as String?
        println(" [.] Got '$response'")
        return response?: "none"
    }
}
