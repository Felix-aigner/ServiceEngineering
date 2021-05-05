package at.serviceengineering.microservice.rental.services

import at.serviceengineering.microservice.rental.entities.Rental
import at.serviceengineering.microservice.rental.repositories.RentalRepository
import org.slf4j.LoggerFactory

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
@Transactional
class RentalService(
        private val rentalRepository: RentalRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun save(rental: Rental): Rental {
        log.debug("Request to save Rental : $rental")
        return rentalRepository.save(rental)
    }

    @Transactional(readOnly = true)
    fun findAll(): MutableList<Rental> {
        log.debug("Request to get all Rentals")
        return rentalRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun findOne(id: String): Optional<Rental> {
        log.debug("Request to get Rental : $id")
        return rentalRepository.findById(id)
    }

    @Transactional(readOnly = true)
    fun findOneEntity(id: String): Rental {
        log.debug("Request to get Rental : $id")
        return rentalRepository.findById(id).get()
    }

    @Transactional(readOnly = true)
    fun saveEntity(rental: Rental): Rental {
        log.debug("Request to get Rental : $rental")
        return rentalRepository.save(rental)
    }

    @Transactional
    fun findAllByCarId(id: UUID): MutableList<Rental> {
        log.debug("Request to get all Rentals")
        TODO("Not yet implemented")
    }


    fun delete(id: String): Unit {
        log.debug("Request to delete Rental : $id")
        rentalRepository.deleteById(id)
    }
}
