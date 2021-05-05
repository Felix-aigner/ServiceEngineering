package at.serviceengineering.microservice.rest.service.handler

import at.serviceengineering.microservice.rest.service.models.CarRequest
import com.google.gson.Gson
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CarMessageHandler {

    @Autowired
    private val template: RabbitTemplate? = null

    @Autowired
    private val exchangeRestGetCars: DirectExchange? = null

    @Autowired
    private val exchangeRestAddCar: DirectExchange? = null

    fun send(exchange: DirectExchange, routingKey : String, message: String): String {
        val response = template!!.convertSendAndReceive(exchange.name, routingKey, message) as String?
        println(" [.] Got '$response'")
        return response?: "none"
    }

    fun getCars(currency: String, id: String = ""): String {
        val routingKey = "cars.getCars"
        val message = Gson().toJson(CarRequest(
                id = id,
                currency = currency,
                findAll = id.isEmpty()
        ))
        return send(exchangeRestGetCars!!, routingKey, message)
    }

    fun addCar(car: String): String {
        val routingKey = "cars.addCar"
        return send(exchangeRestAddCar!!, routingKey, car)
    }
}
