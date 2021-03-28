package at.serviceengineering.webservice1.repositories

import at.serviceengineering.webservice1.entities.Car
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ICarRepository: JpaRepository<Car, UUID> {

    fun findCarById(id: UUID): Car?
}
