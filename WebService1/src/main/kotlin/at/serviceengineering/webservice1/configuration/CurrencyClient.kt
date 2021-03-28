package at.serviceengineering.webservice1.configuration

import at.serviceengineering.webservice1.enums.Currency
import at.serviceengineering.webservice1.wsdl.GetRateRequest
import org.springframework.ws.client.core.support.WebServiceGatewaySupport
import at.serviceengineering.webservice1.wsdl.GetRateResponse

class CurrencyClient: WebServiceGatewaySupport() {

    fun getRate(expectedCurrency: Currency): GetRateResponse {
        val getRateRequest = GetRateRequest()
        getRateRequest.setCurrency(expectedCurrency.toString())
        return webServiceTemplate.marshalSendAndReceive(getRateRequest) as GetRateResponse
    }
}
