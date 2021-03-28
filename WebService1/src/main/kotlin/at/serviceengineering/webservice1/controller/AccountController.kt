package at.serviceengineering.webservice1.controller

import at.serviceengineering.webservice1.dtos.AccountCreationDto
import at.serviceengineering.webservice1.dtos.LoginDto
import at.serviceengineering.webservice1.dtos.PasswordChangeDto
import at.serviceengineering.webservice1.dtos.UserDto
import at.serviceengineering.webservice1.exceptions.InvalidLoginCredentialsException
import at.serviceengineering.webservice1.exceptions.TokenNotValidException
import at.serviceengineering.webservice1.exceptions.UsernameAlreadyExistsException
import at.serviceengineering.webservice1.services.IAccountService
import at.serviceengineering.webservice1.services.JwtTokenService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/account")
class AccountController(
        val accountService: IAccountService,
        val jwtTokenService: JwtTokenService
) {

    @PostMapping
    fun createAccount(@RequestBody accountCreation: AccountCreationDto): ResponseEntity<*> {
        return try {
            accountService.createAccount(accountCreation)
            ResponseEntity.ok().body("")

        } catch (e: UsernameAlreadyExistsException) {
            ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody loginDto: LoginDto): ResponseEntity<*> {
        return try {
            accountService.login(loginDto)
            val user = accountService.getUserDtoByUsername(loginDto.username)
            ResponseEntity.ok().body(user)

        } catch (e: InvalidLoginCredentialsException) {
            ResponseEntity(e.message, HttpStatus.UNAUTHORIZED)
        } catch (e: Exception) {
            ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/delete")
    fun deleteAccount(
            @RequestHeader("token") token: String,
            @RequestBody userDto: UserDto
    ): ResponseEntity<*> {
        return try {
            jwtTokenService.validateUserToken(token, userDto.username)
            accountService.deleteAccount(userDto)
            ResponseEntity.ok().body("")

        } catch (e: TokenNotValidException) {
            ResponseEntity(e.message, HttpStatus.FORBIDDEN)
        } catch (e: Exception) {
            ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/password")
    fun changePassword(
            @RequestHeader("token") token: String,
            @RequestBody passwordChangeDto: PasswordChangeDto
    ): ResponseEntity<*> {
        return try {
            jwtTokenService.validateUserToken(token, passwordChangeDto.username)
            accountService.changePassword(passwordChangeDto)
            ResponseEntity.ok().body("")

        } catch (e: TokenNotValidException) {
            ResponseEntity(e.message, HttpStatus.FORBIDDEN)
        } catch (e: Exception) {
            ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
