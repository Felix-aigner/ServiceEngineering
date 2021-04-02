package at.serviceengineering.webservice1.services

import at.serviceengineering.webservice1.configuration.CurrencyClient
import at.serviceengineering.webservice1.configuration.CurrencyRate
import at.serviceengineering.webservice1.configuration.xPathECB
import at.serviceengineering.webservice1.enums.Currency
import at.serviceengineering.webservice1.exceptions.SoapCallException
import org.springframework.stereotype.Service

@Service
class CurrencyConverterService(
        val currencyClient: CurrencyClient,
        val xPathECB: xPathECB
) {

    fun convertCurrency(value: Float, expectedCurrency: Currency): Float {
//        try {
//            val response = currencyClient.getRate(expectedCurrency)
//            value * response.getRate()
//        } catch (e: Exception) {
//            throw SoapCallException()
//        }

        // this should be in the catch block as fallback
        val currencyList = xPathECB.getRates()
        val dollar = currencyList.filter { currencyRate -> currencyRate.currency == Currency.USD.toString() }.firstOrNull()
        val expectedRate = currencyList.filter { currencyRate -> currencyRate.currency == expectedCurrency.toString() }.firstOrNull()

        val euro = value / dollar?.rate!!.toFloat()
        if (expectedCurrency == Currency.EUR) {
            return euro
        }
        return euro * expectedRate?.rate!!.toFloat()
    }

}
