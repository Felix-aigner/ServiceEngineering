package at.serviceengineering.webservice1.services

import at.serviceengineering.webservice1.dtos.AccountDto
import at.serviceengineering.webservice1.dtos.LoginDto
import at.serviceengineering.webservice1.dtos.PasswordChangeDto
import at.serviceengineering.webservice1.dtos.UserDto
import at.serviceengineering.webservice1.entities.Account
import at.serviceengineering.webservice1.exceptions.InvalidLoginCredentialsException
import at.serviceengineering.webservice1.exceptions.TokenNotValidException
import at.serviceengineering.webservice1.exceptions.UsernameAlreadyExistsException
import at.serviceengineering.webservice1.mapper.AccountMapper
import at.serviceengineering.webservice1.repositories.IAccountRepository
import at.serviceengineering.webservice1.utils.HashUtil.hash
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(
        val accountRepository: IAccountRepository,
        val accountMapper: AccountMapper,
        val jwtTokenGenerator: JwtTokenGenerator
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

    @Transactional
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

    fun usernameAlreadyExists(username: String): Boolean = accountRepository.findAccountByUsername(username) != null

    @Transactional
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

    override fun validateUserToken(token: String, username: String) {
        val jwt = jwtTokenGenerator.recoverJWT(token)
        val uuid = accountRepository.findAccountByUsername(username).let { account -> account?.id.toString() }
        if(jwt.isExpired || jwt.subject != uuid) throw TokenNotValidException()
    }
}
