package at.serviceengineering.microservice.car.repositories

import at.serviceengineering.microservice.car.entities.Car
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CarRepository: MongoRepository<Car, String> {
}