package at.serviceengineering.webservice1.controller

import at.serviceengineering.webservice1.dtos.RentalDTO
import at.serviceengineering.webservice1.exceptions.AccountNotFoundException
import at.serviceengineering.webservice1.exceptions.TokenNotValidException
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
class RentalResource(
        private val jwtTokenService: JwtTokenService,
        private val rentalService: RentalService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * `POST  /rentals` : Create a new rental.
     *
     * @param rentalDTO the rentalDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new rentalDTO, or with status `400 (Bad Request)` if the rental has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/add")
    fun createRental(@RequestHeader("token") token: String, @RequestBody rentalDTO: RentalDTO): ResponseEntity<*> {
        log.debug("REST request to save Rental : $rentalDTO")
        return try {
            if (rentalDTO.id != null) {
                throw Exception()
            }
            rentalService.save(rentalDTO)
            ResponseEntity.ok().body("")
        } catch (e: AccountNotFoundException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
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
    @PutMapping("")
    fun updateRental(@RequestHeader("token") token: String, @RequestBody rentalDTO: RentalDTO): ResponseEntity<RentalDTO> {
        log.debug("REST request to update Rental : $rentalDTO")
        return try {
            jwtTokenService.getAccountFromToken(token)
            val result = rentalService.save(rentalDTO)
            ResponseEntity.ok().body(result)

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
    @GetMapping("/list")
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
