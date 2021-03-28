package at.serviceengineering.webservice1.controller

import at.serviceengineering.webservice1.exceptions.TokenNotValidException
import at.serviceengineering.webservice1.services.AccountService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/car")
class CarController(
        val accountService: AccountService
) {

    @GetMapping
    fun carList(@RequestHeader("token") token: String,
                @RequestHeader("username") username: String): ResponseEntity<*> {
        return try {
            accountService.validateUserToken(token, username)
            ResponseEntity.ok().body("")

        } catch (e: TokenNotValidException) {
            ResponseEntity(e.message, HttpStatus.FORBIDDEN)
        } catch (e: Exception) {
            ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
