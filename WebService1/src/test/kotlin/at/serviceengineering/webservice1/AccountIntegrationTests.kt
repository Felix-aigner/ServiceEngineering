package at.serviceengineering.webservice1

import at.serviceengineering.webservice1.dtos.AccountCreationDto
import at.serviceengineering.webservice1.dtos.LoginDto
import at.serviceengineering.webservice1.dtos.AccountDto
import net.bytebuddy.utility.RandomString
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import java.net.URI

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountIntegrationTests(
        @Autowired val restTemplate: TestRestTemplate
) {
/*
    @Test
    fun `Assert account creation status code 200`() {
        val newAccount = AccountCreationDto(
                username = RandomString().nextString(),
                password = "pw",
                firstname = "firstname",
                lastname = "lastname"
        )
        val entity = restTemplate.postForEntity(URI("/account"), newAccount, Class::class.java)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `Assert account creation with already existing username status code 400`() {
        val newAccount = AccountCreationDto(
                username = RandomString().nextString(),
                password = "pw",
                firstname = "firstname",
                lastname = "lastname"
        )
        val entity = restTemplate.postForEntity(URI("/account"), newAccount, Class::class.java)
        val entity2 = restTemplate.postForEntity(URI("/account"), newAccount, String::class.java)
        assertThat(entity2.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `Assert account login status code 200`() {
        val newAccount = AccountCreationDto(
                username = RandomString().nextString(),
                password = "pw",
                firstname = "firstname",
                lastname = "lastname"
        )
        restTemplate.postForEntity(URI("/account"), newAccount, Class::class.java)

        val login = LoginDto(
                username = newAccount.username,
                password = newAccount.password
        )

        val entity2 = restTemplate.postForEntity(URI("/account/login"), login, AccountDto::class.java)
        val response: AccountDto? = entity2.body
        assertThat(entity2.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response?.token).isNotEmpty()
    }

//    @Test
//    fun `Assert account login with wrong pw status code 401`() {
//        val newAccount = AccountCreationDto(
//                username = RandomString().nextString(),
//                password = "pw",
//                firstname = "firstname",
//                lastname = "lastname"
//        )
//        restTemplate.postForEntity(URI("/account"), newAccount, Class::class.java)
//
//        val login = LoginDto(
//                username = newAccount.username,
//                password = "wrong"
//        )
//
//        val entity2 = restTemplate.postForEntity(URI("/account/login"), login, String::class.java)
//        assertThat(entity2.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
//    }


 */
}
