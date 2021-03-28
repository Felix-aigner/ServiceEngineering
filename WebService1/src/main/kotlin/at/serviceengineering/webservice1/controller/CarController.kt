package at.serviceengineering.webservice1.controller

import at.serviceengineering.webservice1.entities.Car
import at.serviceengineering.webservice1.exceptions.TokenNotValidException
import at.serviceengineering.webservice1.services.AccountService
import at.serviceengineering.webservice1.services.CarService
import at.serviceengineering.webservice1.services.JwtTokenService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/car")
class CarController(
        private val jwtTokenService: JwtTokenService,
        private val carService: CarService
) {

    @GetMapping
    fun carList(@RequestHeader("token") token: String,
                @RequestHeader("username") username: String): ResponseEntity<List<Car>> {
        return try {
            jwtTokenService.validateUserToken(token, username)
            val carList = carService.findAll()
            ResponseEntity.ok().body(carList)

        } catch (e: TokenNotValidException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }
}
