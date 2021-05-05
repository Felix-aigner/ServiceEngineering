package at.serviceengineering.microservice.car.configuration

import org.apache.ws.security.WSConstants
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import org.springframework.ws.client.support.interceptor.ClientInterceptor
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor


@Configuration
class CurrencyClientConfig {

    @Value("\${webservice2.url}")
    lateinit var webservice2Url: String

    @Bean
    fun marshaller(): Jaxb2Marshaller {
        val jaxb2Marshaller = Jaxb2Marshaller()
        jaxb2Marshaller.contextPath = "at.serviceengineering.webservice2.wsdl"
        return jaxb2Marshaller
    }

    @Value("\${soap.admin.name}")
    private val clientToken: String? = null

    @Value("\${soap.admin.secret}")
    private val clientSecret: String? = null

    @Bean
    fun securityInterceptor(): Wss4jSecurityInterceptor? {
        val wss4jSecurityInterceptor = Wss4jSecurityInterceptor()
        wss4jSecurityInterceptor.setSecurementActions("UsernameToken")
        wss4jSecurityInterceptor.setSecurementUsername(clientToken)
        wss4jSecurityInterceptor.setSecurementPassword(clientSecret)
        wss4jSecurityInterceptor.setSecurementUsernameTokenNonce(true)
        wss4jSecurityInterceptor.setSecurementPasswordType(WSConstants.PW_DIGEST)
        return wss4jSecurityInterceptor
    }

    @Bean
    fun currencyClient(jaxb2Marshaller: Jaxb2Marshaller?): CurrencyClient {
        val currencyClient = CurrencyClient()
        currencyClient.setDefaultUri(webservice2Url)
        currencyClient.setMarshaller(jaxb2Marshaller)
        currencyClient.setUnmarshaller(jaxb2Marshaller)
        val interceptor: ClientInterceptor? = securityInterceptor()
        currencyClient.setInterceptors(arrayOf(interceptor))
        return currencyClient
    }
}
