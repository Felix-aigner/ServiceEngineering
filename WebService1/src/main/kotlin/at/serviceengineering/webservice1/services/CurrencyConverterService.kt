package at.serviceengineering.webservice1.services

import at.serviceengineering.webservice1.configuration.CurrencyClient
import at.serviceengineering.webservice1.enums.Currency
import at.serviceengineering.webservice1.exceptions.SoapCallException
import org.springframework.stereotype.Service

@Service
class CurrencyConverterService(
        val currencyClient: CurrencyClient
) {

    fun convertCurrency(value: Float, expectedCurrency: Currency): Float {
        return if(expectedCurrency == Currency.USD)
            value
        else {
            try {
                val response = currencyClient.getRate(expectedCurrency)
                value * response.getRate()
            } catch(e: Exception) {
                throw SoapCallException()
            }

        }
    }


}
