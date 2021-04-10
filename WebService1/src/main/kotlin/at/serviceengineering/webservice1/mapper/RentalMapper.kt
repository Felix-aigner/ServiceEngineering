package at.serviceengineering.webservice1.mapper;

import at.serviceengineering.webservice1.dtos.RentalDTO;
import at.serviceengineering.webservice1.entities.Rental
import org.springframework.stereotype.Service;
import java.lang.NullPointerException

@Service
class RentalMapper(

) {

    fun toEntity(rentalDto: RentalDTO): Rental {
        return Rental(
                id = null,
                rentalDto.startDate,
                rentalDto.endDate,
                rentalDto.car?: throw NullPointerException()
        )
    }

    fun toDto(rental: Rental): RentalDTO {
        return RentalDTO(
                rental.id,
                rental.startDate,
                rental.endDate,
                rental.car,
        )
    }
}

