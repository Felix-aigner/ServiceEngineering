package at.serviceengineering.webservice1.controller

import at.serviceengineering.webservice1.dtos.CarDto
import at.serviceengineering.webservice1.dtos.CarReservationUpdateDto
import at.serviceengineering.webservice1.dtos.ChangeCarRequestDto
import at.serviceengineering.webservice1.entities.Car
import at.serviceengineering.webservice1.exceptions.*
import at.serviceengineering.webservice1.services.ICarService
import at.serviceengineering.webservice1.services.JwtTokenService
import at.serviceengineering.webservice1.wsdl.Currency
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/cars")
class CarController(
        private val jwtTokenService: JwtTokenService,
        private val carService: ICarService
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

    @PutMapping("/book")
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
        } catch (e: CarNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        } catch (e: CarAlreadyRentedException) {
            throw ResponseStatusException(HttpStatus.CONFLICT, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    @PutMapping("/return")
    fun returnCar(@RequestHeader("token") token: String,
                @RequestBody request: CarReservationUpdateDto
    ): ResponseEntity<*> {
        return try {
            val account = jwtTokenService.getAccountFromToken(token)
            carService.returnCar(account, request)
            ResponseEntity.ok().body("")

        } catch (e: TokenNotValidException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: AccountNotFoundException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: CarNotFoundException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, e.message)
        } catch (e: InvalidCarStatusManipulationException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    @PostMapping("/add")
    fun addCar(@RequestHeader("token") token: String,
                @RequestBody car: Car
    ): ResponseEntity<*> {
        return try {
            jwtTokenService.getAccountFromToken(token).also {
                account -> if(!account.isAdministrator) throw TokenNotValidException()
            }
            carService.addCarToDatabase(car)
            ResponseEntity.ok().body("")

        } catch (e: TokenNotValidException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: AccountNotFoundException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    @PostMapping("/change")
    fun changeCar(@RequestHeader("token") token: String,
               @RequestBody car: ChangeCarRequestDto
    ): ResponseEntity<*> {
        return try {
            jwtTokenService.getAccountFromToken(token).also {
                account -> if(!account.isAdministrator) throw TokenNotValidException()
            }
            carService.changeCar(car)
            ResponseEntity.ok().body("")

        } catch (e: TokenNotValidException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: AccountNotFoundException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }
}
