package at.serviceengineering.microservice.rest.service.handler

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AccountMessageHandler {

    @Autowired
    private val template: RabbitTemplate? = null


    fun send(name: String): String {
        println(" [x] Requesting ($name)")
        val response = template!!.convertSendAndReceive("rest.getName", "user.getName", name) as String?
        println(" [.] Got '$response'")
        return response?: "none"
    }
}
