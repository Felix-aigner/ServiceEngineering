package at.serviceengineering.microservice.rental.controller

import at.serviceengineering.microservice.rental.entities.Rental
import at.serviceengineering.microservice.rental.entities.RentalRequest
import at.serviceengineering.microservice.rental.services.RentalService
import at.serviceengineering.microservice.rental.utils.Response
import com.google.gson.Gson
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class RentalRabbitConsumer(
        val rentalService: RentalService,
        val rentalRabbitProducer: RentalRabbitProducer
) {

    val logger: Logger = LoggerFactory.getLogger(RentalRabbitConsumer::class.java)

    @RabbitListener(queues = ["cars.getRentals.requests"])
    fun receiveRentalRequests(obj: String): String {
        logger.info(" [X] Received Get Rentals: '$obj'")

        val request = Gson().fromJson(obj, RentalRequest::class.java)

        return try {
            if (request.method == "findAll") {
                Gson().toJson(rentalService.findAll())
            } else {
                Gson().toJson(rentalService.findOne(request.id!!))
            }
        } catch (e: Exception) {
            logger.error(" [-] Failed Get Rentals: '$obj' ${e.message}")
            Response.FAILED.name
        }
    }

    @RabbitListener(queues = ["rentals.requestRental.requests"])
    fun receiveEditRentalRequests(obj: String): String {
        logger.info(" [X] Received Rental Request: '$obj'")
        return try {
            val request = Gson().fromJson(obj, RentalRequest::class.java)
            when (request.method) {
                "create" -> createRental(request.rental ?: throw Exception())
                "return" -> returnRental(request.id ?: throw Exception())
                "delete" -> deleteRental(request.id ?: throw Exception())
                else -> throw Exception()
            }
        } catch (e: Exception) {
            logger.error(" [-] Failed Edit Rental: '$obj'")
            Response.FAILED.name
        }
    }

    private fun deleteRental(id: String): String {
        rentalService.delete(id)
        return Response.OK.name
    }

    private fun returnRental(id: String): String {
        val rental = rentalService.findOne(id)
        rental.isActive = false
        return Gson().toJson(rentalService.save(rental))
    }

    private fun createRental(rental: Rental): String {
        if(rentalRabbitProducer.getCar(rental.carId) == Response.FAILED.name){
            throw Exception()
        }
        if(rentalRabbitProducer.isAccount(rental.userId) == Response.FAILED.name){
            throw Exception()
        }
        val rentals = rentalService.findAllByCarId(rental.carId)
        rentals?.forEach {
            if(it.isActive)
                throw Exception()
        }

        return Gson().toJson(rentalService.save(rental))
    }
}
