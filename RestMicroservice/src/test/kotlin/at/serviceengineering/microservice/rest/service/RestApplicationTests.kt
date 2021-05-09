package at.serviceengineering.microservice.rest.service

import at.serviceengineering.microservice.rest.service.models.AccountResponse
import com.google.gson.Gson
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.http.client.SimpleClientHttpRequestFactory
import java.net.URI
import java.util.*
import javax.annotation.PostConstruct

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestApplicationTests (@Autowired val restTemplate: TestRestTemplate) {
	@PostConstruct
	fun init() {
		val requestFactory = SimpleClientHttpRequestFactory()
		requestFactory.setOutputStreaming(false)
		restTemplate.restTemplate.requestFactory = requestFactory

		createAccountAndSetToken()
	}

	val account = "{'id':'123','username':'123','password':'123','firstname':'firstname','lastname':'lastname','isAdministrator':false}"
	var token = ""

	fun createAccountAndSetToken () {
		restTemplate.postForEntity(URI("/accounts"), account, String::class.java)
		val res = Gson().fromJson(restTemplate.postForEntity(URI("/accounts/login"), account, String::class.java).body, AccountResponse::class.java)
		token = res.token
	}

	@Test
	fun getAllAccounts() {
		val headers = HttpHeaders()
		headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)
		headers.set("token", token)

		val entity = HttpEntity(null, headers)

		val res = restTemplate.exchange(URI("/accounts"), HttpMethod.GET, entity, String::class.java)
		Assertions.assertThat(res.statusCode).isEqualTo(HttpStatus.OK)
	}
}
