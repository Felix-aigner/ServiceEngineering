package at.serviceengineering.microservice.car.service


import at.serviceengineering.microservice.car.configuration.CurrencyClient
import at.serviceengineering.microservice.car.exceptions.SoapCallException
import at.serviceengineering.webservice2.wsdl.Currency
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class CurrencyConverterService(
        private val currencyClient: CurrencyClient
) {

    fun convertCurrency(value: BigDecimal, startCurrency: Currency , expectedCurrency: Currency): BigDecimal {
        try {
            val response = currencyClient.getRate(value, startCurrency,  expectedCurrency)
            return response.currencyConversion.targetValue.setScale(2, RoundingMode.HALF_EVEN)
        } catch (e: Exception) {
            throw SoapCallException()
        }
    }

}
