package at.serviceengineering.microservice.rest.service.controller


import at.serviceengineering.microservice.rest.service.handler.AccountMessageHandler

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("/accounts")
class AccountController(
        val accountMessageHandler: AccountMessageHandler
) {

    @GetMapping("/test")
    fun testRabbit(): ResponseEntity<*> {
        return try {
//            val test = "Test"
//            val response = accountMessageHandler.sendTestMessage(test)
            val test = accountMessageHandler.send("hallo")
            ResponseEntity.ok().body(test)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }
/*
    @GetMapping
    fun getAllAccounts (@RequestHeader("token") token: String
    ): ResponseEntity<List<AccountDto>> {
        return try {
            jwtTokenService.getAccountFromToken(token)
            val accountList = accountService.findAll()
            ResponseEntity.ok().body(accountList)

        } catch (e: TokenNotValidException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: AccountNotFoundException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    @GetMapping("/{id}")
    fun getAccount(@RequestHeader("token") token: String,  @PathVariable id: UUID): ResponseEntity<AccountDto> {
        return try {
            jwtTokenService.getAccountFromToken(token)
            val accountDto = accountService.findOne(id)
            ResponseEntity.ok().body(accountDto)

        } catch (e: TokenNotValidException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: AccountNotFoundException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }


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

    @DeleteMapping("/{id}")
    fun deleteAccount(
            @RequestHeader("token") token: String,
            @PathVariable id: UUID): ResponseEntity<*> {
        return try {
            jwtTokenService.getAccountFromToken(token)
            accountService.deleteAccount(id)
            ResponseEntity.ok().body("")

        } catch (e: TokenNotValidException) {
            ResponseEntity(e.message, HttpStatus.FORBIDDEN)
        } catch (e: Exception) {
            ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PutMapping("/change-password")
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

 */
}