package at.serviceengineering.webservice1.services

import at.serviceengineering.webservice1.enums.Currency
import org.springframework.stereotype.Service

@Service
class CurrencyConverterService {

    fun convertCurrency(value: Float, expectedCurrency: Currency): Float {
        return if(expectedCurrency == Currency.USD)
            value
        else {
            // TODO: Implement WebService2 Currency Converter
            value
        }

    }
}
