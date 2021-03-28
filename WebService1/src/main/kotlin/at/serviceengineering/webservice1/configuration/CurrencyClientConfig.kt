package at.serviceengineering.webservice1.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.oxm.jaxb.Jaxb2Marshaller


@Configuration
class CurrencyClientConfig {
    @Bean
    fun marshaller(): Jaxb2Marshaller {
        val jaxb2Marshaller = Jaxb2Marshaller()
        jaxb2Marshaller.contextPath = "at.serviceengineering.webservice1.wsdl"
        return jaxb2Marshaller
    }

    @Bean
    fun currencyClient(jaxb2Marshaller: Jaxb2Marshaller?): CurrencyClient {
        val currencyClient = CurrencyClient()
        currencyClient.setDefaultUri("http://localhost:6000/ws/currency")
        currencyClient.setMarshaller(jaxb2Marshaller)
        currencyClient.setUnmarshaller(jaxb2Marshaller)
        return currencyClient
    }
}
