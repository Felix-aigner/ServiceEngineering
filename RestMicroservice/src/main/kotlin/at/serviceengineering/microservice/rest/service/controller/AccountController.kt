package at.serviceengineering.microservice.rest.service.controller


import at.serviceengineering.microservice.rest.service.exceptions.InvalidLoginCredentialsException
import at.serviceengineering.microservice.rest.service.exceptions.TokenNotValidException
import at.serviceengineering.microservice.rest.service.exceptions.UsernameAlreadyExistsException
import at.serviceengineering.microservice.rest.service.handler.AccountMessageHandler
import at.serviceengineering.microservice.rest.service.handler.JwtTokenHandler
import com.google.gson.Gson

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("/accounts")
class AccountController(
        val accountMessageHandler: AccountMessageHandler,
        val jwtTokenHandler: JwtTokenHandler
) {

    @GetMapping
    fun getAllAccounts (@RequestHeader("token") token: String?
    ): ResponseEntity<String> {
        return try {
            jwtTokenHandler.recoverJWT(token?: throw Exception())
            val accounts = accountMessageHandler.getAccounts()
            ResponseEntity.ok().body(accounts)
        } catch (e: TokenNotValidException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    @GetMapping("/{id}")
    fun getAccount(@RequestHeader("token") token: String?,  @PathVariable id: String): ResponseEntity<String> {
        return try {
            jwtTokenHandler.recoverJWT(token?: throw Exception())
            val accounts = accountMessageHandler.getAccounts(id)
            ResponseEntity.ok().body(accounts)
        } catch (e: TokenNotValidException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    @PostMapping
    fun createAccount(@RequestBody accountCreation: String): ResponseEntity<*> {
        return try {
            val account = accountMessageHandler.createAccount(accountCreation)
            if(account.equals("username already exists")){
                throw UsernameAlreadyExistsException()
            }
            ResponseEntity.ok().body(account)
        } catch (e: UsernameAlreadyExistsException) {
            ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody login: String): ResponseEntity<*> {
        return try {
            val account = accountMessageHandler.login(login)
            val token = jwtTokenHandler.buildJwt(account.id)

            ResponseEntity.ok().body(account.toAccountResponse(token))
        } catch (e: InvalidLoginCredentialsException) {
            ResponseEntity(e.message, HttpStatus.UNAUTHORIZED)
        } catch (e: Exception) {
            ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @DeleteMapping("/{id}")
    fun deleteAccount(
            @RequestHeader("token") token: String?,
            @PathVariable id: String): ResponseEntity<*> {
        return try {
            jwtTokenHandler.recoverJWT(token?: throw Exception())
            val response = accountMessageHandler.deleteAccount(id)
            if(response.equals("failed")){
                throw Exception()
            }
            ResponseEntity.ok().body("")
        } catch (e: TokenNotValidException) {
            ResponseEntity(e.message, HttpStatus.FORBIDDEN)
        } catch (e: Exception) {
            ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
