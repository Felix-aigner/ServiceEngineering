package at.serviceengineering.webservice1.mapper

import at.serviceengineering.webservice1.dtos.AccountCreationDto
import at.serviceengineering.webservice1.dtos.AccountDto
import at.serviceengineering.webservice1.entities.Account
import at.serviceengineering.webservice1.entities.Rental
import at.serviceengineering.webservice1.services.JwtTokenService
import at.serviceengineering.webservice1.utils.HashUtil.hash
import org.springframework.stereotype.Service

@Service
class AccountMapper(
        val jwtTokenService: JwtTokenService
) {

    fun mapToEntityAndHashPassword(accountCreationDto: AccountCreationDto): Account {
        return Account(
                id = null,
                username = accountCreationDto.username,
                password = hash(accountCreationDto.password),
                firstname = accountCreationDto.firstname,
                lastname = accountCreationDto.lastname,
                isAdministrator = false,
                rentals = null
        )
    }

    fun mapToDtoAndGenerateJwt(account: Account): AccountDto {
        return AccountDto(
                account.id?: throw java.lang.NullPointerException(),
                account.username,
                account.firstname,
                account.lastname,
                jwtTokenService.buildJwt(account.id?: throw NullPointerException("account_id should never be null")),
                account.isAdministrator,
                account.rentals
        )
    }

    fun toDto(account: Account): AccountDto {
        return AccountDto(
                account.id?: throw java.lang.NullPointerException(),
                account.username,
                account.firstname,
                account.lastname,
                null,
                account.isAdministrator,
                account.rentals
        )
    }
}
