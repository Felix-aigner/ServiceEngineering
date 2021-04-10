package at.serviceengineering.webservice1.controller

import at.serviceengineering.webservice1.dtos.RentalDTO
import at.serviceengineering.webservice1.exceptions.*
import at.serviceengineering.webservice1.mapper.RentalMapper
import at.serviceengineering.webservice1.services.CarService
import at.serviceengineering.webservice1.services.JwtTokenService
import at.serviceengineering.webservice1.services.RentalService

import org.slf4j.LoggerFactory

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

import java.net.URISyntaxException
import java.util.*

private const val ENTITY_NAME = "rental"
/**
 * REST controller for managing [com.se.project.domain.Rental].
 */
@RestController
@RequestMapping("/rentals")
class RentalController(
        private val jwtTokenService: JwtTokenService,
        private val rentalMapper: RentalMapper,
        private val rentalService: RentalService,
        private val carService: CarService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * `POST  /rentals` : Create a new rental.
     *
     * @param rentalDTO the rentalDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new rentalDTO, or with status `400 (Bad Request)` if the rental has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping
    fun createRental(@RequestHeader("token") token: String,
                @RequestBody request: RentalDTO
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

    /**
     * `PUT  /rentals` : Updates an existing rental.
     *
     * @param rentalDTO the rentalDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated rentalDTO,
     * or with status `400 (Bad Request)` if the rentalDTO is not valid,
     * or with status `500 (Internal Server Error)` if the rentalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    fun updateRental(@RequestHeader("token") token: String, @PathVariable id: UUID): ResponseEntity<*> {
        log.debug("REST request to update Rental : $id")
        return try {
            val account = jwtTokenService.getAccountFromToken(token)
            carService.returnCar(account, id)
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

    /**
     * `GET  /rentals` : get all the rentals.
     *

     * @param filter the filter of the request.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of rentals in body.
     */
    @GetMapping
    fun getAllRentals(@RequestHeader("token") token: String, @RequestParam(required = false) filter: String?): MutableList<RentalDTO> {
        log.debug("REST request to get all Rentals")
        return try {
            jwtTokenService.getAccountFromToken(token)
            rentalService.findAll()

        } catch (e: TokenNotValidException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: AccountNotFoundException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    /**
     * `GET  /rentals` : get all the rentals.
     *

     * @param filter the filter of the request.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of rentals in body.
     */
    @GetMapping("/my-rentals")
    fun getUserRentals(@RequestHeader("token") token: String): List<RentalDTO> {
        log.debug("REST request to get all Rentals")
        return try {
            val account = jwtTokenService.getAccountFromToken(token)
            account.rentals?.map { rental -> rentalMapper.toDto(rental)}?: listOf()
        } catch (e: TokenNotValidException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: AccountNotFoundException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    /**
     * `GET  /rentals/:id` : get the "id" rental.
     *
     * @param id the id of the rentalDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the rentalDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("{id}")
    fun getRental(@RequestHeader("token") token: String, @PathVariable id: UUID): ResponseEntity<RentalDTO> {
        log.debug("REST request to get Rental : $id")
        return try {
            jwtTokenService.getAccountFromToken(token)
            val rentalDTO = rentalService.findOne(id)
            ResponseEntity.ok().body(rentalDTO.get())

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
    @DeleteMapping("{id}")
    fun deleteRental(@RequestHeader("token") token: String, @PathVariable id: UUID): ResponseEntity<Void> {
        log.debug("REST request to delete Rental : $id")

        return try {
            jwtTokenService.getAccountFromToken(token).also {
                account -> if(!account.isAdministrator) throw TokenNotValidException()
            }
            rentalService.delete(id)
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
