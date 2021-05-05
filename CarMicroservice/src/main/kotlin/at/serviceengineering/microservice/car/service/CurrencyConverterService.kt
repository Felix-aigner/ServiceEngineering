package at.serviceengineering.microservice.car.service


import at.serviceengineering.microservice.car.configuration.CurrencyClient
import at.serviceengineering.microservice.car.exceptions.SoapCallException
import at.serviceengineering.webservice2.wsdl.Currency
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class CurrencyConverterService(
        val currencyClient: CurrencyClient
) {

    fun convertCurrency(value: BigDecimal, startCurrency: Currency , expectedCurrency: Currency): BigDecimal {
        try {
            val response = currencyClient.getRate(value, startCurrency,  expectedCurrency)
            return response.currencyConversion.targetValue
        } catch (e: Exception) {
            throw SoapCallException()
        }
    }

}
