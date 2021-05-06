package at.serviceengineering.microservice.rest.service.controller


import at.serviceengineering.microservice.rest.service.exceptions.TokenNotValidException
import at.serviceengineering.microservice.rest.service.handler.RentalMessageHandler
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("/rentals")
class RentalController(
        val rentalMessageHandler: RentalMessageHandler
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping("/create")
    fun createRental(
            @RequestHeader("token") token: String?,
            @RequestBody body: String
    ): ResponseEntity<*> {
        logger.debug("REST request to create Rental : $body")

        return try {
//            val account = jwtTokenService.getAccountFromToken(token)
            val response = rentalMessageHandler.rentalRequest(body)
            ResponseEntity.ok().body(response)

        } catch (e: TokenNotValidException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        }  catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    @PostMapping("/return")
    fun returnRental(
            @RequestHeader("token") token: String?,
            @RequestBody body: String
    ): ResponseEntity<*> {
        logger.debug("REST request to update Rental : $body")

        return try {
//            val account = jwtTokenService.getAccountFromToken(token)
            val response = rentalMessageHandler.rentalRequest(body)
            ResponseEntity.ok().body(response)

        } catch (e: TokenNotValidException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    @DeleteMapping
    fun deleteRental(
            @RequestHeader("token") token: String?,
            @RequestBody body: String
    ): ResponseEntity<*> {
        logger.debug("REST request to delete Rental : $body")

        return try {
//            jwtTokenService.getAccountFromToken(token)
            val response = rentalMessageHandler.rentalRequest(body)
            ResponseEntity.ok().body(response)

        } catch (e: TokenNotValidException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }
}
