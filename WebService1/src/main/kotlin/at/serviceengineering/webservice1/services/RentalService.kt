package at.serviceengineering.webservice1.services
import at.serviceengineering.webservice1.dtos.RentalDTO
import at.serviceengineering.webservice1.entities.Rental
import at.serviceengineering.webservice1.mapper.RentalMapper
import at.serviceengineering.webservice1.repositories.IRentalRepository
import org.slf4j.LoggerFactory

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
@Transactional
class RentalService(
        private val rentalRepository: IRentalRepository,
        private val rentalMapper: RentalMapper
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun save(rentalDTO: RentalDTO): Rental {
        log.debug("Request to save Rental : $rentalDTO")

        val rental = rentalMapper.toEntity(rentalDTO)
        return rentalRepository.save(rental)
    }

    @Transactional(readOnly = true)
    fun findAll(): MutableList<RentalDTO> {
        log.debug("Request to get all Rentals")
        return rentalRepository.findAll()
            .mapTo(mutableListOf(), rentalMapper::toDto)            
    }

    @Transactional(readOnly = true)
    fun findOne(id: UUID): Optional<RentalDTO> {
        log.debug("Request to get Rental : $id")
        return rentalRepository.findById(id)
            .map(rentalMapper::toDto)
    }

    @Transactional(readOnly = true)
    fun findOneEntity(id: UUID): Rental {
        log.debug("Request to get Rental : $id")
        return rentalRepository.findById(id).get()
    }

    @Transactional(readOnly = true)
    fun saveEntity(rental: Rental): Rental {
        log.debug("Request to get Rental : $rental")
        return rentalRepository.save(rental)
    }


    fun delete(id: UUID): Unit {
        log.debug("Request to delete Rental : $id")

        rentalRepository.deleteById(id)
    }
}
