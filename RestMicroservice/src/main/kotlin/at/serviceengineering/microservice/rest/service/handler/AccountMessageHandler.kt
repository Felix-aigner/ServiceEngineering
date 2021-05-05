package at.serviceengineering.microservice.rest.service.handler

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AccountMessageHandler {

    @Autowired
    private val template: RabbitTemplate? = null


    fun send(exchange: String, routingKey : String): String {
        val response = template!!.convertSendAndReceive(exchange, routingKey, "hans") as String?
        println(" [.] Got '$response'")
        return response?: "none"
    }

    fun getName(): String {
        val exchange = "rest.getName"
        val routingKey = "user.getName"
        return send(exchange, routingKey)
    }

    fun getAllAccounts(): String {
        val exchange = "rest.getAllAccounts"
        val routingKey = "user.getAllAccounts"
        return send(exchange, routingKey)
    }
}
