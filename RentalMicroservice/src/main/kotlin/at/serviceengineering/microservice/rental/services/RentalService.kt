package at.serviceengineering.microservice.rental.services

import at.serviceengineering.microservice.rental.entities.Rental
import at.serviceengineering.microservice.rental.repositories.RentalRepository
import org.slf4j.LoggerFactory

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class RentalService(
        private val rentalRepository: RentalRepository
) {

    fun save(rental: Rental): Rental = rentalRepository.save(rental)

    fun findAll(): List<Rental> = rentalRepository.findAll()

    fun findOne(id: String): Rental = rentalRepository.findById(id).get()

    fun findAllByCarId(carId: String): List<Rental>? = rentalRepository.findByCarId(carId)

    fun delete(id: String) = rentalRepository.deleteById(id)
}
