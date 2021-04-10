package at.serviceengineering.webservice1.configuration


import at.serviceengineering.webservice1.wsdl.Currency
import at.serviceengineering.webservice1.wsdl.GetCurrencyConversionRequest
import at.serviceengineering.webservice1.wsdl.GetCurrencyConversionResponse

import org.springframework.ws.client.core.support.WebServiceGatewaySupport
import java.math.BigDecimal

class CurrencyClient: WebServiceGatewaySupport() {

    fun getRate(baseValue: BigDecimal, startCurrency: Currency, expectedCurrency: Currency): GetCurrencyConversionResponse {
        val getRateRequest = GetCurrencyConversionRequest()
        getRateRequest.baseCurrency = startCurrency
        getRateRequest.baseValue = baseValue
        getRateRequest.targetCurrency = expectedCurrency
        return webServiceTemplate.marshalSendAndReceive(getRateRequest) as GetCurrencyConversionResponse
    }
}
