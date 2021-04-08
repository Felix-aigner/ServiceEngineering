package at.serviceengineering.webservice1.services

import at.serviceengineering.webservice1.configuration.CurrencyClient
import at.serviceengineering.webservice1.exceptions.SoapCallException
import at.serviceengineering.webservice1.wsdl.Currency
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class CurrencyConverterService(
        val currencyClient: CurrencyClient
) {

    fun convertCurrency(value: BigDecimal, expectedCurrency: Currency): BigDecimal {
        try {
            val response = currencyClient.getRate(value, expectedCurrency)
            return response.currencyConversion.targetValue
        } catch (e: Exception) {
            throw SoapCallException()
        }
    }

}
