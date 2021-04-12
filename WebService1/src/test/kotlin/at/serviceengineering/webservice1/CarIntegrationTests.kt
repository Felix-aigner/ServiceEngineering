package at.serviceengineering.webservice1

import org.assertj.core.api.Assertions.assertThat
import at.serviceengineering.webservice1.dtos.AccountCreationDto
import at.serviceengineering.webservice1.dtos.AccountDto
import at.serviceengineering.webservice1.dtos.LoginDto
import at.serviceengineering.webservice1.entities.Car
import at.serviceengineering.webservice1.services.AccountService
import at.serviceengineering.webservice1.services.CarService
import at.serviceengineering.webservice1.wsdl.Currency
import net.bytebuddy.utility.RandomString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.HttpClientErrorException
import java.net.URI
import java.util.*
import javax.annotation.PostConstruct

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarIntegrationTests(
        @Autowired val restTemplate: TestRestTemplate,
        @Autowired val accountService: AccountService,
        @Autowired val carService: CarService
) {
    @PostConstruct
    fun init() {
        val requestFactory = SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        restTemplate.restTemplate.requestFactory = requestFactory;
    }

    @Test
    fun addCar() {
        //setup
        val newAccount = createAdminAccountInclTokenRetrieval().first;

        val car = Car(
                id = UUID.randomUUID(),
                type = "TestType",
                brand = "TestBrand",
                kwPower = 100,
                usdPrice = 100.toBigDecimal(),
                isRented = false
        )

        val headers = HttpHeaders()
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)
        headers.set("token", newAccount!!.token);

        val entity = HttpEntity(car, headers)

        //testcase
        val addCarResponse = restTemplate.exchange(URI("/cars"), HttpMethod.POST, entity, String::class.java)
        assertThat(addCarResponse.statusCode).isEqualTo(HttpStatus.OK);

        //teardown
        singleAccountTeardown(newAccount.username)
        singleCarTeardown(car.id)
    }

    @Test
    fun `HTTP 403 at adding a car as non-admin`() {
        //setup
        val newAccount = createValidAccountInclTokenRetrieval().first;

        val car = Car(
                id = UUID.randomUUID(),
                type = "TestType",
                brand = "TestBrand",
                kwPower = 100,
                usdPrice = 100.toBigDecimal(),
                isRented = false
        )

        val headers = HttpHeaders()
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)
        headers.set("token", newAccount!!.token);

        val entity = HttpEntity(car, headers)

        //testcase
        val addCarResponse = restTemplate.exchange(URI("/cars"), HttpMethod.POST, entity, String::class.java)
        assertThat(addCarResponse.statusCode).isEqualTo(HttpStatus.FORBIDDEN);

        //teardown
        singleAccountTeardown(newAccount.username)
        singleCarTeardown(car.id!!)
    }

    @Test
    fun `HTTP 403 at adding a car with invalid token`() {
        //setup
        val car = Car(
                id = UUID.randomUUID(),
                type = "TestType",
                brand = "TestBrand",
                kwPower = 100,
                usdPrice = 100.toBigDecimal(),
                isRented = false
        )

        val headers = HttpHeaders()
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)
        headers.set("token", "invalid token");

        val entity = HttpEntity(car, headers)

        //testcase
        try {
            restTemplate.exchange(URI("/cars"), HttpMethod.POST, entity, String::class.java)
        } catch (e: HttpClientErrorException){
            assertThat(e.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
        }

        //teardown
        singleCarTeardown(car.id!!)
    }

    @Test
    fun `HTTP 403 at car list retrieval with invalid token`() {
        val headers = HttpHeaders()
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)
        headers.set("token", "invalid token");

        val entity = HttpEntity("", headers)

        try {
            restTemplate.exchange(URI("/cars"), HttpMethod.GET, entity, String::class.java)
        } catch (e: HttpClientErrorException){
            assertThat(e.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
        }
    }

    @Test
    fun carListRetrieval() {
        //setup
        val newAccount = createValidAccountInclTokenRetrieval().first;

        val headers = HttpHeaders()
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)
        headers.set("token", newAccount!!.token);

        val entity = HttpEntity("", headers)

        //testcase
        val carListResponse = restTemplate.exchange(URI("/cars"), HttpMethod.GET, entity, Class::class.java)
        assertThat(carListResponse.statusCode).isEqualTo(HttpStatus.OK);

        //teardown
        singleAccountTeardown(newAccount.username)
    }

    fun createValidAccountInclTokenRetrieval(): Pair<AccountDto?, String> {
        //new Account
        val newAccount = AccountCreationDto(
                username = RandomString().nextString(),
                password = "pw",
                firstname = "firstname",
                lastname = "lastname"
        )

        val createHttpEntity = HttpEntity(newAccount)
        val createResponse = restTemplate.exchange(URI("/accounts"), HttpMethod.POST, createHttpEntity, Class::class.java)
        assertThat(createResponse.statusCode).isEqualTo(HttpStatus.OK)

        //login account to retrieve token
        val login = LoginDto(
                username = newAccount.username,
                password = newAccount.password
        )
        val loginHttpEntity = HttpEntity(login)

        val loginResponse = restTemplate.exchange(URI("/accounts/login"), HttpMethod.POST, loginHttpEntity, AccountDto::class.java)
        assertThat(loginResponse.statusCode).isEqualTo(HttpStatus.OK)
        val responseBody: AccountDto? = loginResponse.body
        assertThat(responseBody?.token).isNotEmpty()

        return Pair(responseBody, newAccount.password);
    }

    fun createAdminAccountInclTokenRetrieval(): Pair<AccountDto?, String> {

        //new Account
        val newAccount = AccountCreationDto(
                username = RandomString().nextString(),
                password = "pw",
                firstname = "firstname",
                lastname = "lastname"
        )

        accountService.createAdministrator(newAccount);

        //login account to retrieve token
        val login = LoginDto(
                username = newAccount.username,
                password = newAccount.password
        )
        val loginHttpEntity = HttpEntity(login)

        val loginResponse = restTemplate.exchange(URI("/accounts/login"), HttpMethod.POST, loginHttpEntity, AccountDto::class.java)
        assertThat(loginResponse.statusCode).isEqualTo(HttpStatus.OK)
        val responseBody: AccountDto? = loginResponse.body
        assertThat(responseBody?.token).isNotEmpty()

        return Pair(responseBody, newAccount.password);
    }

    fun singleAccountTeardown(username: String) {
        val accountsBefore = accountService.findAll().size;

        accountService.deleteAccountByUsername(username)

        val accountsAfter = accountService.findAll().size;

        assertThat(accountsAfter).isEqualTo(accountsBefore-1);
    }

    fun singleCarTeardown(id: UUID?) {//hängt sich derweil auf in carService.findAll und deleteCar
        val carsBefore = carService.findAll(Currency.USD).size

        carService.deleteCar(id!!)

        val carsAfter = carService.findAll(Currency.USD).size

        assertThat(carsAfter).isEqualTo(carsBefore-1);
    }
}