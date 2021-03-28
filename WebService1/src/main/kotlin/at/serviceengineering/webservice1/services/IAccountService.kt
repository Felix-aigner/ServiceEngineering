package at.serviceengineering.webservice1.services

import at.serviceengineering.webservice1.dtos.AccountCreationDto
import at.serviceengineering.webservice1.dtos.LoginDto
import at.serviceengineering.webservice1.dtos.PasswordChangeDto
import at.serviceengineering.webservice1.dtos.UserDto
import at.serviceengineering.webservice1.entities.Account

interface IAccountService {

    fun login(loginDto: LoginDto)

    fun changePassword(passwordChangeDto: PasswordChangeDto)

    fun createAccount(accountCreationDto: AccountCreationDto)

    fun deleteAccount(userDto: UserDto)

    fun findAll(): List<Account>

    fun getUserDtoByUsername(username: String): UserDto
}
