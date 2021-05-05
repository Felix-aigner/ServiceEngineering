package at.serviceengineering.microservice.rest.service.handler

import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CarMessageHandler {

    @Autowired
    private val template: RabbitTemplate? = null

    fun send(exchange: String, routingKey : String, message: String): String {
        val response = template!!.convertSendAndReceive(exchange, routingKey, message) as String?
        println(" [.] Got '$response'")
        return response?: "none"
    }

    fun getCars(id: String = ""): String {
        val exchange = "rest.getCars"
        val routingKey = "cars.getCars"
        return send(exchange, routingKey, id)
    }

    fun addCar(car: String): String {
        val exchange = "rest.addCar"
        val routingKey = "cars.addCar"
        return send(exchange, routingKey, car)
    }

    @RabbitHandler
    fun receive(obj: String) {

        println("Received: '$obj'")
    }
}