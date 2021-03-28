package at.serviceengineering.webservice1.mapper

import at.serviceengineering.webservice1.dtos.AccountCreationDto
import at.serviceengineering.webservice1.dtos.UserDto
import at.serviceengineering.webservice1.entities.Account
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
                accountCreationDto.username,
                hash(accountCreationDto.password),
                accountCreationDto.firstname,
                accountCreationDto.lastname,
                rentedCars = null
        )
    }

    fun mapToDtoAndGenerateJwt(account: Account): UserDto {
        return UserDto(
                account.username,
                account.firstname,
                account.lastname,
                jwtTokenService.buildJwt(account.id?: throw NullPointerException("account_id should never be null"))
        )
    }
}
