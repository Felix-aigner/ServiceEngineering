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

    @RabbitListener(queues = ["rentals.getRentals.requests"])
    fun receiveRentalRequests(obj: String): String {
        logger.info(" [X] Received Get Rentals: '$obj'")
        return try {
            if(obj.toLowerCase().equals("getallrentals")) {
                val rentals = try {
                    rentalService.findAll()
                } catch (e: Exception) {
                    ""
                }
                Gson().toJson(rentals)
            } else {
                val rentals = try {
                    rentalService.findOne(obj)
                } catch (e: Exception) {
                    ""
                }
                Gson().toJson(rentals)
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
                "return" -> returnRental(request.rental ?: throw Exception())
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

    private fun returnRental(rental: Rental): String {
        var rental = rentalService.findOne(rental.id?: throw Exception())
        rental.isActive = false
        rental = rentalService.save(rental)
        return Gson().toJson(rental)
    }

    private fun createRental(rental: Rental): String {
        if(rentalRabbitProducer.getCar(rental.carId) == Response.FAILED.name){
            logger.info("CAR ID NOT FOUND IN CAR MICROSERVICE")
            throw Exception()
        }
        if(rentalRabbitProducer.isAccount(rental.userId) == Response.FAILED.name){
            logger.info("USER ID NOT FOUND IN USER MICROSERVICE")
            throw Exception()
        }
        try {
            val rentals = rentalService.findAllByCarId(rental.carId)
            rentals?.forEach {
                if(it.isActive)
                    throw Exception()
            }
        } catch (e: Exception) {
            logger.info("no rentals")
        }

        return Gson().toJson(rentalService.save(rental))
    }
}
