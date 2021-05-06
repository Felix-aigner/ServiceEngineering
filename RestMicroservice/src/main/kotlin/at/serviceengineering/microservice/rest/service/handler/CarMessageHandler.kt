package at.serviceengineering.microservice.rest.service.handler

import at.serviceengineering.microservice.rest.service.models.CarRequest
import at.serviceengineering.microservice.rest.service.util.Response
import com.google.gson.Gson
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CarMessageHandler {

    val logger: Logger = LoggerFactory.getLogger(CarMessageHandler::class.java)

    @Autowired
    private val template: RabbitTemplate? = null

    @Autowired
    private val exchangeRestGetCars: DirectExchange? = null

    @Autowired
    private val exchangeRestEditCar: DirectExchange? = null

    fun send(exchange: DirectExchange, routingKey : String, message: String): String {
        logger.info(" [X] Send to '${exchange.name}' -> '$message'")
        val response = template!!.convertSendAndReceive(exchange.name, routingKey, message) as String?
        logger.info(" [.] Got  '$response'")
        return response?: throw Exception()
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

    fun editCar(message: String): String {
        val routingKey = "cars.editCar"
        return send(exchangeRestEditCar!!, routingKey, message)
    }
}
