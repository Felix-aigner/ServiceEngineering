package at.serviceengineering.microservice.rest.service.handler

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RentalMessageHandler {

    val logger: Logger = LoggerFactory.getLogger(RentalMessageHandler::class.java)

    @Autowired
    private val template: RabbitTemplate? = null

    @Autowired
    private val exchangeRestGetRentals: DirectExchange? = null

    @Autowired
    private val exchangeRestRequestRental: DirectExchange? = null

    fun send(exchange: DirectExchange, routingKey : String, message: String): String {
        logger.info(" [X] Send to '${exchange.name}' -> '$message'")
        val response = template!!.convertSendAndReceive(exchange.name, routingKey, message) as String?
        logger.info(" [.] Got  '$response'")
        return response?: throw Exception()
    }

    fun getRentals(id: String = "getAllRentals"): String {
        val routingKey = "rentals.getRentals"
        return send(exchangeRestGetRentals!!, routingKey, id)
    }

    fun editRental(message: String): String {
        val routingKey = "rentals.requestRental"
        return send(exchangeRestRequestRental!!, routingKey, message)
    }
}
