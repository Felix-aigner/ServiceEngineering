package at.serviceengineering.webservice1

import org.assertj.core.api.Assertions.assertThat
import at.serviceengineering.webservice1.dtos.AccountCreationDto
import at.serviceengineering.webservice1.dtos.LoginDto
import at.serviceengineering.webservice1.dtos.UserDto
import at.serviceengineering.webservice1.entities.Car
import at.serviceengineering.webservice1.services.AccountService
import net.bytebuddy.utility.RandomString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.web.client.HttpClientErrorException
import java.net.URI
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarIntegrationTests(
        @Autowired val restTemplate: TestRestTemplate,
        @Autowired val accountService: AccountService
) {
    @Test
    fun carListRetrieval() {
        val newAccount = createValidAccountInclTokenRetrieval().first;

        val headers = HttpHeaders()
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)
        headers.set("token", newAccount!!.token);

        val entity = HttpEntity("", headers)

        val carListResponse = restTemplate.exchange(URI("/car/list"), HttpMethod.GET, entity, String::class.java)
        assertThat(carListResponse.statusCode).isEqualTo(HttpStatus.OK);
    }

    @Test
    fun addCar() {
        val newAccount = createAdminAccountInclTokenRetrieval().first;

        val car = Car(
                id = UUID.randomUUID(),
                type = "TestType",
                brand = "TestBrand",
                kwPower = 100,
                usdPrice = 100.toFloat(),
                isRented = false
        )

        val headers = HttpHeaders()
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)
        headers.set("token", newAccount!!.token);

        val entity = HttpEntity(car, headers)

        val addCarResponse = restTemplate.exchange(URI("/car/add"), HttpMethod.POST, entity, String::class.java)
        assertThat(addCarResponse.statusCode).isEqualTo(HttpStatus.OK);
    }

    @Test
    fun `HTTP 403 at adding a car as non-admin`() {
        val newAccount = createValidAccountInclTokenRetrieval().first;

        val car = Car(
                id = UUID.randomUUID(),
                type = "TestType",
                brand = "TestBrand",
                kwPower = 100,
                usdPrice = 100.toFloat(),
                isRented = false
        )

        val headers = HttpHeaders()
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)
        headers.set("token", newAccount!!.token);

        val entity = HttpEntity(car, headers)

        val addCarResponse = restTemplate.exchange(URI("/car/add"), HttpMethod.POST, entity, String::class.java)
        assertThat(addCarResponse.statusCode).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    fun `HTTP 403 at adding a car with invalid token`() {
        val car = Car(
                id = UUID.randomUUID(),
                type = "TestType",
                brand = "TestBrand",
                kwPower = 100,
                usdPrice = 100.toFloat(),
                isRented = false
        )

        val headers = HttpHeaders()
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)
        headers.set("token", "invalid token");

        val entity = HttpEntity(car, headers)

        try {
            restTemplate.exchange(URI("/car/add"), HttpMethod.POST, entity, String::class.java)
        } catch (e: HttpClientErrorException){
            assertThat(e.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
        }
    }

    @Test
    fun `HTTP 403 at car list retrieval with invalid token`() {
        val headers = HttpHeaders()
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)
        headers.set("token", "invalid token");

        val entity = HttpEntity("", headers)

        try {
            restTemplate.exchange(URI("/car/list"), HttpMethod.GET, entity, String::class.java)
        } catch (e: HttpClientErrorException){
            assertThat(e.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
        }
    }

    fun createValidAccountInclTokenRetrieval(): Pair<UserDto?, String> {

        //new Account
        val newAccount = AccountCreationDto(
                username = RandomString().nextString(),
                password = "pw",
                firstname = "firstname",
                lastname = "lastname"
        )

        val createHttpEntity = HttpEntity(newAccount)
        val createResponse = restTemplate.exchange(URI("/account"), HttpMethod.POST, createHttpEntity, Class::class.java)
        assertThat(createResponse.statusCode).isEqualTo(HttpStatus.OK)

        //login account to retrieve token
        val login = LoginDto(
                username = newAccount.username,
                password = newAccount.password
        )
        val loginHttpEntity = HttpEntity(login)

        val loginResponse = restTemplate.exchange(URI("/account/login"), HttpMethod.POST, loginHttpEntity, UserDto::class.java)
        assertThat(loginResponse.statusCode).isEqualTo(HttpStatus.OK)
        val responseBody: UserDto? = loginResponse.body
        assertThat(responseBody?.token).isNotEmpty()

        return Pair(responseBody, newAccount.password);
    }

    fun createAdminAccountInclTokenRetrieval(): Pair<UserDto?, String> {

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

        val loginResponse = restTemplate.exchange(URI("/account/login"), HttpMethod.POST, loginHttpEntity, UserDto::class.java)
        assertThat(loginResponse.statusCode).isEqualTo(HttpStatus.OK)
        val responseBody: UserDto? = loginResponse.body
        assertThat(responseBody?.token).isNotEmpty()

        return Pair(responseBody, newAccount.password);
    }
}