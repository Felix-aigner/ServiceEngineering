package at.serviceengineering.webservice1.services

import at.serviceengineering.webservice1.dtos.AccountCreationDto
import at.serviceengineering.webservice1.dtos.LoginDto
import at.serviceengineering.webservice1.dtos.PasswordChangeDto
import at.serviceengineering.webservice1.dtos.AccountDto
import at.serviceengineering.webservice1.entities.Account
import java.util.*

interface IAccountService {

    fun login(loginDto: LoginDto)

    fun changePassword(passwordChangeDto: PasswordChangeDto)

    fun createAccount(accountCreationDto: AccountCreationDto)

    fun deleteAccount(id: UUID)

    fun deleteAccountByUsername(username: String)

    fun findAll(): List<AccountDto>

    fun findOne(id : UUID): AccountDto

    fun getUserDtoByUsername(username: String): AccountDto
}
