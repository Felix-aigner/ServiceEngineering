package at.serviceengineering.microservice.user.service

import at.serviceengineering.microservice.user.entities.Account
import at.serviceengineering.microservice.user.repositories.AccountRepository
import at.serviceengineering.microservice.user.utils.HashUtil.hash
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AccountService(
        private val accountRepository: AccountRepository,

) {

    val logger: Logger = LoggerFactory.getLogger(AccountService::class.java)

    fun login(account: Account) {
        if (!passwordCorrect(account.username, account.password)){
            TODO("Not yet implemented")
        } else
            TODO("Not yet implemented")
    }


    fun passwordCorrect(username: String, password: String): Boolean {
        return when (val account = accountRepository.findByUsername(username)) {
            null -> false
            else -> account.password == hash(password)
        }
    }

    fun createAccount(account: Account) {
        if (usernameAlreadyExists(account.username)) {
            TODO("Not yet implemented")
        }
        account.password = hash(account.password)
        accountRepository.save(account).also {
            logger.info("New Account created: $it")
        }
    }

    fun deleteAccount(id: String) {
        accountRepository.deleteById(id).also {
            logger.info("Deleted Account with UserId: ${id}")
        }
    }

    fun findAll(): List<Account> = accountRepository.findAll()

    fun findOne(id: String): Account {
        return accountRepository.findById(id).get()
    }

    fun createAdministrator(account: Account) {
        account.password = hash(account.password)
        accountRepository.save(account).also {
            logger.info("New Account created: $it")
        }
    }

    private fun usernameAlreadyExists(username: String): Boolean {
        return accountRepository.findByUsername(username) != null
    }
}
