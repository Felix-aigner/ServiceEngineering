package at.serviceengineering.microservice.rest.service.controller


import at.serviceengineering.microservice.rest.service.exceptions.TokenNotValidException
import at.serviceengineering.microservice.rest.service.handler.RentalMessageHandler
import at.serviceengineering.microservice.rest.service.util.Response
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

    @GetMapping
    fun getRentals(@RequestHeader("token") token: String?
    ): ResponseEntity<String> {
        return try {
            //jwtTokenHandler.recoverJWT(token?: throw Exception())
            val rentals = rentalMessageHandler.getRentals()
            ResponseEntity.ok().body(rentals)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    @GetMapping("/{id}")
    fun getRental(@RequestHeader("token") token: String?,  @PathVariable id: String): ResponseEntity<String> {
        return try {
            //jwtTokenHandler.recoverJWT(token?: throw Exception())
            val rentals = rentalMessageHandler.getRentals(id)
            ResponseEntity.ok().body(rentals)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    @PostMapping("/create")
    fun createRental(
            @RequestHeader("token") token: String?,
            @RequestBody body: String
    ): ResponseEntity<*> {
        logger.debug("REST request to create Rental : $body")

        return try {
//            val account = jwtTokenService.getAccountFromToken(token)
            val response = rentalMessageHandler.editRental(body)
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
            val response = rentalMessageHandler.editRental(body)
            ResponseEntity.ok().body(response)

        } catch (e: TokenNotValidException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    @PutMapping
    fun changeRental(
            @RequestHeader("token") token: String?,
            @RequestBody body: String
    ): ResponseEntity<*> {
        return try {
//            jwtTokenService.getAccountFromToken(token)
            val response = rentalMessageHandler.editRental(body)
            if(response == Response.FAILED.name){
                throw Exception()
            }
            ResponseEntity.ok().body("")
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
            val response = rentalMessageHandler.editRental(body)
            ResponseEntity.ok().body(response)

        } catch (e: TokenNotValidException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }
}
