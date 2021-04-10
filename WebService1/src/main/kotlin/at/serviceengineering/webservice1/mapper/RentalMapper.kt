package at.serviceengineering.webservice1.mapper;

import at.serviceengineering.webservice1.dtos.RentalDTO;
import at.serviceengineering.webservice1.entities.Rental
import at.serviceengineering.webservice1.repositories.ICarRepository
import at.serviceengineering.webservice1.services.CarService
import at.serviceengineering.webservice1.wsdl.Currency
import org.springframework.stereotype.Service;
import java.lang.NullPointerException

@Service
class RentalMapper( private val carMapper: CarMapper
) {

    fun toEntity(rentalDto: RentalDTO): Rental {
        return Rental(
                id = null,
                rentalDto.startDate,
                rentalDto.endDate,
                rentalDto.isActive,
                carMapper.mapToCarWithCustomCurrency(rentalDto.car)
        )
    }

    fun toDto(rental: Rental): RentalDTO {
        return RentalDTO(
                rental.id,
                rental.startDate,
                rental.endDate,
                rental.isActive,
                carMapper.mapToCarDtoWithCustomCurrency(rental.car, requestedCurrency = Currency.USD)
        )
    }
}

