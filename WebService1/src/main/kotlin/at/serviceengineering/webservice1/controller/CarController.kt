package at.serviceengineering.webservice1.controller

import at.serviceengineering.webservice1.dtos.CarDto
import at.serviceengineering.webservice1.dtos.CarReservationUpdateDto
import at.serviceengineering.webservice1.enums.Currency
import at.serviceengineering.webservice1.exceptions.AccountNotFoundException
import at.serviceengineering.webservice1.exceptions.CarDoesNotExistException
import at.serviceengineering.webservice1.exceptions.TokenNotValidException
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

    @GetMapping("/list")
    fun carList(@RequestHeader("token") token: String,
                @RequestParam(name = "cy", required = false) currency: Currency?
    ): ResponseEntity<List<CarDto>> {
        return try {
            jwtTokenService.getAccountFromToken(token)
            val carList = carService.findAll(currency?: Currency.USD)
            ResponseEntity.ok().body(carList)

        } catch (e: TokenNotValidException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: AccountNotFoundException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    @PutMapping("")
    fun bookCar(@RequestHeader("token") token: String,
                @RequestBody request: CarReservationUpdateDto
    ): ResponseEntity<*> {
        return try {
            val account = jwtTokenService.getAccountFromToken(token)
            carService.bookCar(account, request)
            ResponseEntity.ok().body("")

        } catch (e: TokenNotValidException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: AccountNotFoundException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: CarDoesNotExistException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }
}