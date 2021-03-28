package at.serviceengineering.webservice1.mapper

import at.serviceengineering.webservice1.dtos.AccountDto
import at.serviceengineering.webservice1.dtos.UserDto
import at.serviceengineering.webservice1.entities.Account
import at.serviceengineering.webservice1.services.JwtTokenGenerator
import at.serviceengineering.webservice1.utils.HashUtil.hash
import org.springframework.stereotype.Service

@Service
class AccountMapper(
        val jwtTokenGenerator: JwtTokenGenerator
) {

    fun mapToEntityAndHashPassword(accountDto: AccountDto): Account {
        return Account(
                id = null,
                accountDto.username,
                hash(accountDto.password),
                accountDto.firstname,
                accountDto.lastname
        )
    }

    fun mapToDtoAndGenerateJwt(account: Account): UserDto {
        return UserDto(
                account.username,
                account.firstname,
                account.lastname,
                jwtTokenGenerator.buildJwt(account.id?: throw NullPointerException("account_id should not be null"))
        )
    }
}
