package at.serviceengineering.webservice1.mapper

import at.serviceengineering.webservice1.dtos.CarDto
import at.serviceengineering.webservice1.entities.Car
import at.serviceengineering.webservice1.enums.Currency
import at.serviceengineering.webservice1.exceptions.SoapCallException
import at.serviceengineering.webservice1.services.CurrencyConverterService
import org.springframework.stereotype.Service

@Service
class CarMapper(
        private val currencyConverterService: CurrencyConverterService
) {

    fun mapToCarDtoWithCustomCurrency(car: Car, requestedCurrency: Currency): CarDto {
        var currency = requestedCurrency
        var price: Float
        try {
            price = currencyConverterService.convertCurrency(car.usdPrice, currency)
        } catch (e: SoapCallException) {
            currency = Currency.USD
            price = car.usdPrice
        }

        return CarDto(
                id = car.id,
                type = car.type,
                brand = car.brand,
                kwPower = car.kwPower,
                currency = currency,
                isRented = car.isRented,
                price = price
        )
    }
}
