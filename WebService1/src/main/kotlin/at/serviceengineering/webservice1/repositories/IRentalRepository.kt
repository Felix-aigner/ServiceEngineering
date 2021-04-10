package at.serviceengineering.webservice1.repositories

import at.serviceengineering.webservice1.entities.Car
import at.serviceengineering.webservice1.entities.Rental
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Suppress("unused")
@Repository
interface IRentalRepository : JpaRepository<Rental, UUID> {

    fun findByCarId(id: UUID): MutableList<Rental>
}
