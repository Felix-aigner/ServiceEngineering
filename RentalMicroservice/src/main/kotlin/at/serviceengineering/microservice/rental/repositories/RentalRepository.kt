package at.serviceengineering.microservice.rental.repositories

import at.serviceengineering.microservice.rental.entities.Rental
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface RentalRepository: MongoRepository<Rental, String> {
    fun findByCarId(id: String) : List<Rental>?
}