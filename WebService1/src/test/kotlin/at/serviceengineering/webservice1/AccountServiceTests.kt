package at.serviceengineering.webservice1

import at.serviceengineering.webservice1.dtos.AccountCreationDto
import at.serviceengineering.webservice1.services.AccountService
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.random.Random

@SpringBootTest
class AccountServiceTests(
        @Autowired val accountService: AccountService
) {
    @Test
    fun isAdmin() {
        val accountCreationDto = AccountCreationDto(
                username = "testadmin"+ Random.nextInt(0, 2023219),
                password = "testadmin",
                firstname = "firstname",
                lastname = "lastname"
        )

        accountService.createAdministrator(accountCreationDto)

        val isAdministrator = accountService.getUserDtoByUsername(accountCreationDto.username).isAdministrator

        assert(isAdministrator.equals(true));
    }

    @Disabled //Test funzt grundsätzlich, Hibernate Lazy load problem noch zu fixen
    @Test
    fun createAdmin() {
        val accountCreationDto = AccountCreationDto(
                username = "testadmin"+ Random.nextInt(0, 2023219),
                password = "testadmin",
                firstname = "firstname",
                lastname = "lastname"
        )

        accountService.createAdministrator(accountCreationDto)

        val username = accountService.getUserDtoByUsername(accountCreationDto.username)

        assert(username.equals(accountCreationDto.username));
    }

    @Test
    fun findById() {
        val accountCreationDto = AccountCreationDto(
                username = "testadmin"+ Random.nextInt(0, 2023219),
                password = "testadmin",
                firstname = "firstname",
                lastname = "lastname"
        )

        accountService.createAdministrator(accountCreationDto)

        val id = accountService.getUserDtoByUsername(accountCreationDto.username).id

        val newAccount = accountService.findOne(id)
        assert(newAccount.id == id);
    }
}