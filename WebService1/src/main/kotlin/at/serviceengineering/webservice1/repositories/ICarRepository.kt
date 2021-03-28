package at.serviceengineering.webservice1.repositories

import at.serviceengineering.webservice1.entities.Car
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional
interface ICarRepository: JpaRepository<Car, UUID> {

    fun findCarById(id: UUID): Car?
}
