package at.serviceengineering.webservice1.mapper

import at.serviceengineering.webservice1.dtos.CarDto
import at.serviceengineering.webservice1.entities.Car
import at.serviceengineering.webservice1.exceptions.SoapCallException
import at.serviceengineering.webservice1.services.CurrencyConverterService
import at.serviceengineering.webservice1.wsdl.Currency
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class CarMapper(
        private val currencyConverterService: CurrencyConverterService
) {

    fun mapToCarDtoWithCustomCurrency(car: Car, requestedCurrency: Currency): CarDto {
        var currency = requestedCurrency
        var price: BigDecimal
        try {
            price = currencyConverterService.convertCurrency(car.usdPrice, Currency.USD , currency)
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

    fun mapToCarWithCustomCurrency(car: CarDto): Car {
        val price = currencyConverterService.convertCurrency(car.price, car.currency, Currency.USD)
        return Car(
                id = car.id,
                type = car.type,
                brand = car.brand,
                kwPower = car.kwPower,
                isRented = car.isRented,
                usdPrice = price
        )
    }
}
