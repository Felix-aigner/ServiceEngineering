package at.serviceengineering.microservice.user

import at.serviceengineering.microservice.user.entities.Account
import at.serviceengineering.microservice.user.service.AccountService
import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserMicroserviceApplicationTests {

	@Autowired
	val accountService: AccountService? = null

	val account = Account("testId", "testUsername", "testPassword", "testFirstname", "testLastname", false)
	val account2 = Account("testId2", "testUsername2", "testPassword", "testFirstname", "testLastname", false)
	val account3 = Account("testId3", "testUsername3", "testPassword", "testFirstname", "testLastname", false)

	@BeforeEach
	fun setup() {
		cleanUp()
	}

	@AfterEach
	fun teardown() {
		cleanUp()
	}

	fun cleanUp() {
		var accounts = accountService?.findAll()

		if (accounts != null) {
			for(account in accounts) {
				accountService?.deleteAccount(account.id)
			}
		}

		accounts = accountService?.findAll()

		assert(accounts?.isEmpty()!!)
	}

	@Test
	fun createAccount() {
		var accounts = accountService?.findAll()
		assert(accounts?.size == 0)

		accountService?.createAccount(account)

		accounts = accountService?.findAll()
		assert(accounts?.size == 1)
	}

	@Test
	fun identicalUsernameThrowsException() {
		accountService?.createAccount(account)

		shouldThrow<Exception> {
			accountService?.createAccount(account)
		}
	}

	@Test
	fun loginWithWrongCredentialsFails() {
		accountService?.createAccount(account)
		account.password = "no"

		shouldThrow<Exception> {
			accountService?.login(account)
		}
	}

	@Test
	fun deleteAccount() {
		accountService?.createAccount(account)
		accountService?.findOne(account.id)
		accountService?.deleteAccount(account.id)

		shouldThrow<Exception> {
			accountService?.findOne(account.id)
		}
	}

	@Test
	fun findAll() {
		accountService?.createAccount(account)
		accountService?.createAccount(account2)
		accountService?.createAccount(account3)

		val accounts = accountService?.findAll()

		assert(accounts!!.contains(account))
		assert(accounts.contains(account2))
		assert(accounts.contains(account3))
	}

	@Test
	fun findOne() {
		accountService?.createAccount(account)

		accountService?.findOne(account.id)
	}
}
