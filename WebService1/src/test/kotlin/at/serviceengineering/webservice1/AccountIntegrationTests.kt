package at.serviceengineering.webservice1

import at.serviceengineering.webservice1.dtos.AccountCreationDto
import at.serviceengineering.webservice1.dtos.LoginDto
import at.serviceengineering.webservice1.dtos.AccountDto
import at.serviceengineering.webservice1.services.AccountService
import net.bytebuddy.utility.RandomString
import org.assertj.core.api.Assertions.assertThat
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
class AccountIntegrationTests(
        @Autowired val restTemplate: TestRestTemplate,
        @Autowired val accountService: AccountService
){
    @PostConstruct
    fun init() {
        val requestFactory = SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        restTemplate.restTemplate.requestFactory = requestFactory;
    }

    @Test
    fun `Assert account creation status code 200`() {
        val newAccount = AccountCreationDto(
                username = RandomString().nextString(),
                password = "pw",
                firstname = "firstname",
                lastname = "lastname"
        )
        val entity = restTemplate.postForEntity(URI("/accounts"), newAccount, Class::class.java)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)

        //teardown
        singleTeardown(newAccount.username);
    }

    @Test
    fun `Assert account creation with already existing username status code 400`() {
        val newAccount = AccountCreationDto(
                username = RandomString().nextString(),
                password = "pw",
                firstname = "firstname",
                lastname = "lastname"
        )
        restTemplate.postForEntity(URI("/accounts"), newAccount, Class::class.java)
        val entity2 = restTemplate.postForEntity(URI("/accounts"), newAccount, String::class.java)
        assertThat(entity2.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun loginSuccessful() {
        //setup
        val newAccount = AccountCreationDto(
                username = RandomString().nextString(),
                password = "pw",
                firstname = "firstname",
                lastname = "lastname"
        )
        restTemplate.postForEntity(URI("/accounts"), newAccount, Class::class.java)

        val login = LoginDto(
                username = newAccount.username,
                password = newAccount.password
        )

        //testcase
        val entity2 = restTemplate.postForEntity(URI("/accounts/login"), login, AccountDto::class.java)
        val response: AccountDto? = entity2.body
        assertThat(entity2.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response?.token).isNotEmpty()

        //teardown
        singleTeardown(newAccount.username);
    }

    @Test
    fun deleteAccount() {
        //setup
        val newAccount = createValidAccountInclTokenRetrieval().first;
        val password = createValidAccountInclTokenRetrieval().second;

        //testcase
        ///delete account
        val headers = HttpHeaders()
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)
        headers.set("token", newAccount?.token);

        val user = AccountDto(
                id = newAccount!!.id,
                username = newAccount.username,
                firstname = newAccount.firstname,
                lastname = newAccount.lastname,
                token = newAccount.token,
                isAdministrator = false,
                rentals = null
        )

        val deleteHttpEntity = HttpEntity(user, headers)
        val deleteResponse = restTemplate.exchange(URI("/accounts/"+user.id), HttpMethod.DELETE, deleteHttpEntity, String::class.java)
        assertThat(deleteResponse.statusCode).isEqualTo(HttpStatus.OK)

        //login fails after deletion
        val login = LoginDto(
                username = newAccount.username,
                password = password
        )
        val loginHttpEntity = HttpEntity(login)

        val res = restTemplate.exchange(URI("/accounts/login"), HttpMethod.POST, loginHttpEntity, String::class.java)
        assertThat(res.statusCode.value()).isEqualTo(401);
    }

    @Test
    fun accountNotFound() {
        val login = LoginDto(
                username = "notExistingUsername",
                password = "pw"
        )

        val res = restTemplate.postForEntity(URI("/accounts/login"), login, String::class.java)
        assertThat(res.statusCode.value()).isEqualTo(401);
    }

    @Test
    fun `HTTP 200 at getAccount`() {
        //setup
        val newAccount = createValidAccountInclTokenRetrieval().first;

        val headers = HttpHeaders()
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)
        headers.set("token", newAccount!!.token);

        val entity = HttpEntity("", headers);

        //testcase
        val res = restTemplate.exchange(URI("/accounts/" + newAccount.id), HttpMethod.GET, entity, String::class.java)
        assertThat(res.statusCode).isEqualTo(HttpStatus.OK)

        //teardown
        singleTeardown(newAccount.username);
    }

    @Test
    fun getAccount() {
        //setup
        val newAccount = createValidAccountInclTokenRetrieval().first;

        val headers = HttpHeaders()
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)
        headers.set("token", newAccount!!.token);

        val entity = HttpEntity("", headers);

        //testcase
        val res = restTemplate.exchange(URI("/accounts/" + newAccount.id), HttpMethod.GET, entity, AccountDto::class.java)
        val accountRes:AccountDto? = res.body;
        assertThat(accountRes?.firstname).isEqualTo(newAccount.firstname)

        //teardown
        singleTeardown(newAccount.username);
    }

    @Test
    fun getAllAccounts() {
        //setup
        val account1 = createValidAccountInclTokenRetrieval().first;
        val account2 = createValidAccountInclTokenRetrieval().first;

        val headers = HttpHeaders()
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)
        headers.set("token", account1!!.token);

        val entity = HttpEntity("", headers);

        //testcase
        val getAccounts = accountService.findAll().size;
        val res = restTemplate.exchange(URI("/accounts"), HttpMethod.GET, entity, List::class.java)
        val accountRes:List<AccountDto>? = res.body as List<AccountDto>?;
        assertThat(accountRes?.size).isEqualTo(getAccounts)

        //teardown
        val createdAccountIds = listOf(account1.id, account2!!.id)
        listTeardown(createdAccountIds)
    }

    @Test
    fun `Assert account login with wrong pw status code 401`() {
        //setup
        val newAccount = AccountCreationDto(
                username = RandomString().nextString(),
                password = "pw",
                firstname = "firstname",
                lastname = "lastname"
        )
        restTemplate.postForEntity(URI("/accounts"), newAccount, Class::class.java)

        val login = LoginDto(
                username = newAccount.username,
                password = "wrong"
        )

        val entity = HttpEntity(login)

        //testcase
        try {
            restTemplate.exchange(URI("/accounts/login"), HttpMethod.POST, entity, String::class.java)
        } catch (e: HttpClientErrorException){
            assertThat(e.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
        }

        //teardown
        singleTeardown(newAccount.username)
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

    fun listTeardown(ids: List<UUID>) {
        val accountsBefore = accountService.findAll().size;

        for (id in ids) {
            accountService.deleteAccount(id);
        }

        val accountsAfter = accountService.findAll().size;

        assertThat(accountsAfter).isEqualTo(accountsBefore-ids.size);
    }

    fun singleTeardown(username: String) {
        val accountsBefore = accountService.findAll().size;

        accountService.deleteAccountByUsername(username)

        val accountsAfter = accountService.findAll().size;

        assertThat(accountsAfter).isEqualTo(accountsBefore-1);
    }
}
