package at.serviceengineering.webservice1.services

import at.serviceengineering.webservice1.dtos.AccountDto
import at.serviceengineering.webservice1.dtos.LoginDto
import at.serviceengineering.webservice1.dtos.PasswordChangeDto
import at.serviceengineering.webservice1.dtos.UserDto
import at.serviceengineering.webservice1.entities.Account
import at.serviceengineering.webservice1.exceptions.InvalidLoginCredentialsException
import at.serviceengineering.webservice1.exceptions.UsernameAlreadyExistsException
import at.serviceengineering.webservice1.mapper.AccountMapper
import at.serviceengineering.webservice1.repositories.IAccountRepository
import at.serviceengineering.webservice1.utils.HashUtil.hash
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AccountService(
        private val accountRepository: IAccountRepository,
        private val accountMapper: AccountMapper,
        private val jwtTokenService: JwtTokenService
) : IAccountService {

    val logger: Logger = LoggerFactory.getLogger(AccountService::class.java)

    override fun login(loginDto: LoginDto) {
        if (!passwordCorrect(loginDto.username, loginDto.password))
            throw InvalidLoginCredentialsException()
    }

    fun passwordCorrect(username: String, password: String): Boolean {
        return when (val account = accountRepository.findAccountByUsername(username)) {
            null -> false
            else -> account.password == hash(password)
        }
    }

    override fun changePassword(passwordChangeDto: PasswordChangeDto) {
        val account = accountRepository.findAccountByUsername(passwordChangeDto.username)
                ?: throw NullPointerException()

        account.password = hash(passwordChangeDto.newPassword)
        accountRepository.save(account).also {
            logger.info("Password changed successfully: $it")
        }
    }

    override fun createAccount(accountDto: AccountDto) {
        if (usernameAlreadyExists(accountDto.username)) {
            throw UsernameAlreadyExistsException()
        }

        val entity = accountMapper.mapToEntityAndHashPassword(accountDto)
        accountRepository.save(entity).also {
            logger.info("New Account created: $it")
        }
    }

    override fun deleteAccount(userDto: UserDto) {
        accountRepository.deleteAccountByUsername(userDto.username).also {
            logger.info("Deleted Account with Username: ${userDto.username}")
        }
    }

    override fun findAll(): List<Account> = accountRepository.findAll()

    override fun getUserDtoByUsername(username: String): UserDto {
        return accountMapper.mapToDtoAndGenerateJwt(
                accountRepository.findAccountByUsername(username) ?: throw NullPointerException()
        )
    }

    private fun usernameAlreadyExists(username: String): Boolean = accountRepository.findAccountByUsername(username) != null
}
