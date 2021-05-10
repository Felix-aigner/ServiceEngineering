package at.serviceengineering.microservice.rest.service

import at.serviceengineering.microservice.rest.service.models.Account
import at.serviceengineering.microservice.rest.service.models.AccountResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
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

	var token = ""
	val headers = HttpHeaders()

	val account = "{'id':'1','username':'1','password':'123','firstname':'firstname','lastname':'lastname','isAdministrator':false}"
	val account2 = "{'id':'2','username':'2','password':'123','firstname':'firstname','lastname':'lastname','isAdministrator':false}"

	val car = "{'id':'1','type':'1','brand':'1','kwPower':999,'price':99}"

	val rental = ""

	@PostConstruct
	fun init() {
		val requestFactory = SimpleClientHttpRequestFactory()
		requestFactory.setOutputStreaming(false)
		restTemplate.restTemplate.requestFactory = requestFactory

		createAccountAndSetToken()
		prepareHeaders()
	}

	fun createAccountAndSetToken () {
		restTemplate.postForEntity(URI("/accounts"), account, String::class.java)
		val res = Gson().fromJson(restTemplate.postForEntity(URI("/accounts/login"), account, String::class.java).body, AccountResponse::class.java)
		token = res.token
	}

	fun prepareHeaders() {
		headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)
		headers.set("token", token)
	}

	@BeforeEach
	fun setup() {
		cleanUp()
	}

	@AfterEach
	fun teardown() {
		cleanUp()
	}

	fun cleanUp() {
		val entity = HttpEntity(null, headers)

		val itemType = object : TypeToken<List<AccountResponse>>() {}.type
		var accountsList = Gson().fromJson<List<AccountResponse>>(restTemplate.exchange(URI("/accounts"), HttpMethod.GET, entity, String::class.java).body, itemType)

		if (accountsList != null) {
			for (account in accountsList) {
				Assertions.assertThat(restTemplate.exchange(URI("/accounts/" + account.id), HttpMethod.DELETE, entity, String::class.java).statusCode).isEqualTo(HttpStatus.OK)
			}
		}

		accountsList = Gson().fromJson<List<AccountResponse>>(restTemplate.exchange(URI("/accounts"), HttpMethod.GET, entity, String::class.java).body, itemType)

		assert(accountsList.isEmpty())
	}

	@Test
	fun getAllAccounts() {
		val entity = HttpEntity(null, headers)

		val res = restTemplate.exchange(URI("/accounts"), HttpMethod.GET, entity, String::class.java)
		Assertions.assertThat(res.statusCode).isEqualTo(HttpStatus.OK)
	}

	@Test
	fun createAccount() {
		val res = Gson().fromJson(restTemplate.postForEntity(URI("/accounts"), account, String::class.java).body, AccountResponse::class.java)
		assert(res.id == Gson().fromJson(account, Account::class.java).id)
	}

	@Test
	fun createMultipleAccounts() {
		val res = Gson().fromJson(restTemplate.postForEntity(URI("/accounts"), account, String::class.java).body, AccountResponse::class.java)
		assert(res.id == Gson().fromJson(account, Account::class.java).id)

		val res2 = Gson().fromJson(restTemplate.postForEntity(URI("/accounts"), account2, String::class.java).body, AccountResponse::class.java)
		assert(res2.id == Gson().fromJson(account2, Account::class.java).id)
	}

	@Test
	fun login() {
		restTemplate.postForEntity(URI("/accounts"), account, String::class.java)
		val res = Gson().fromJson(restTemplate.postForEntity(URI("/accounts/login"), account, String::class.java).body, AccountResponse::class.java)
		assert(res.username == Gson().fromJson(account, Account::class.java).username)
	}

	@Test
	fun retrieveJWTToken() {
		restTemplate.postForEntity(URI("/accounts"), account, String::class.java)
		val res = Gson().fromJson(restTemplate.postForEntity(URI("/accounts/login"), account, String::class.java).body, AccountResponse::class.java)
		token = res.token

		val entity = HttpEntity(null, headers)

		val res2 = restTemplate.exchange(URI("/accounts"), HttpMethod.GET, entity, String::class.java)
		Assertions.assertThat(res2.statusCode).isEqualTo(HttpStatus.OK)
	}
}
