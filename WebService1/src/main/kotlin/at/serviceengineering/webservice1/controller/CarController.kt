package at.serviceengineering.webservice1.controller

import at.serviceengineering.webservice1.dtos.CarDto
import at.serviceengineering.webservice1.dtos.CarReservationUpdateDto
import at.serviceengineering.webservice1.dtos.ChangeCarRequestDto
import at.serviceengineering.webservice1.dtos.RentalDTO
import at.serviceengineering.webservice1.entities.Car
import at.serviceengineering.webservice1.exceptions.*
import at.serviceengineering.webservice1.services.ICarService
import at.serviceengineering.webservice1.services.JwtTokenService
import at.serviceengineering.webservice1.wsdl.Currency
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/cars")
class CarController(
        private val jwtTokenService: JwtTokenService,
        private val carService: ICarService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun getAllCars(@RequestHeader("token") token: String,
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

    @GetMapping("/{id}")
    fun getCar(@RequestHeader("token") token: String,  @PathVariable id: UUID,
                @RequestParam(name = "cy", required = false) currency: Currency?
    ): ResponseEntity<CarDto> {
        return try {
            jwtTokenService.getAccountFromToken(token)
            val carList = carService.findOne(id, currency?: Currency.USD)
            ResponseEntity.ok().body(carList)

        } catch (e: TokenNotValidException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: AccountNotFoundException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    @PostMapping
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

    @PutMapping
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

    /**
     *  `DELETE  /rentals/:id` : delete the "id" rental.
     *
     * @param id the id of the rentalDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/{id}")
    fun deleteCar(
            @RequestHeader("token") token: String,
            @PathVariable id: UUID): ResponseEntity<Void> {
        log.debug("REST request to delete Car : $id")

        return try {
            jwtTokenService.getAccountFromToken(token).also {
                account -> if(!account.isAdministrator) throw TokenNotValidException()
            }
            carService.deleteCar(id)
            ResponseEntity.noContent().build()

        } catch (e: TokenNotValidException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: AccountNotFoundException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }
}
