package at.serviceengineering.webservice1.mapper

import at.serviceengineering.webservice1.dtos.CarDto
import at.serviceengineering.webservice1.entities.Car
import at.serviceengineering.webservice1.enums.Currency
import at.serviceengineering.webservice1.services.CurrencyConverterService
import org.springframework.stereotype.Service

@Service
class CarMapper(
        private val currencyConverterService: CurrencyConverterService
) {

    fun mapToCarDtoWithCustomCurrency(car: Car, currency: Currency): CarDto {
        return CarDto(
                id = car.id,
                type = car.type,
                brand = car.brand,
                kwPower = car.kwPower,
                currency = currency,
                isRented = car.isRented,
                price = currencyConverterService.convertCurrency(car.usdPrice, currency)
        )
    }
}
