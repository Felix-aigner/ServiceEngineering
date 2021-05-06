package at.serviceengineering.microservice.rental.controller

import at.serviceengineering.microservice.rental.models.CarRequest
import com.google.gson.Gson
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RentalRabbitProducer {

    val logger: Logger = LoggerFactory.getLogger(RentalRabbitProducer::class.java)

    @Autowired
    private val template: RabbitTemplate? = null

    @Autowired
    private val exchangeRentalGetCars: DirectExchange? = null

    @Autowired
    private val exchangeRentalIsAccount: DirectExchange? = null

    fun send(exchange: DirectExchange, routingKey : String, message: String): String {
        logger.info(" [X] Send to '${exchange.name}' -> '$message'")
        val response = template!!.convertSendAndReceive(exchange.name, routingKey, message) as String?
        logger.info(" [.] Got  '$response'")
        return response?: throw Exception()
    }

    fun isAccount(message: String): String {
        val routingKey = "user.isAccount"
        return send(exchangeRentalIsAccount!!, routingKey, message)
    }

    fun getCar(id: String): String {
        val message = Gson().toJson(CarRequest(
                id = id,
                currency = "USD",
                findAll = id.isEmpty()
        ))
        val routingKey = "cars.getCars"
        return send(exchangeRentalGetCars!!, routingKey, message)
    }
}
