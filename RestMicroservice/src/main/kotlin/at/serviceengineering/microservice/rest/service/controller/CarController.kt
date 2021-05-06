package at.serviceengineering.microservice.rest.service.controller

import at.serviceengineering.microservice.rest.service.exceptions.TokenNotValidException
import at.serviceengineering.microservice.rest.service.handler.CarMessageHandler
import at.serviceengineering.microservice.rest.service.handler.JwtTokenHandler
import at.serviceengineering.microservice.rest.service.util.Response
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/cars")
class CarController(
        val carMessageHandler: CarMessageHandler,
        val jwtTokenHandler: JwtTokenHandler
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun getCars(
            @RequestParam(name = "id", required = false) id: String?,
            @RequestParam(name = "cy", required = false) currency: String?
    ): ResponseEntity<*> {
        return try {
            val cars = if (id.isNullOrEmpty()) {
                carMessageHandler.getCars(currency ?: "USD")
            } else {
                carMessageHandler.getCars(currency ?: "USD", id)
            }
            ResponseEntity.ok().body(cars)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    @PostMapping
    fun addCar(
            @RequestHeader("token") token: String,
            @RequestBody body: String
    ): ResponseEntity<*> {
        return try {
            jwtTokenHandler.recoverJWT(token?: throw Exception())
            carMessageHandler.editCar(body)
            ResponseEntity.ok().body("")
        } catch (e: TokenNotValidException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }

    @PutMapping
    fun changeCar(
            @RequestHeader("token") token: String,
            @RequestBody body: String
    ): ResponseEntity<*> {
        return try {
            jwtTokenHandler.recoverJWT(token?: throw Exception())
            val response = carMessageHandler.editCar(body)
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
    fun deleteCar(
            @RequestHeader("token") token: String?,
            @RequestBody body: String
    ): ResponseEntity<*> {
        log.debug("REST request to delete Car : $body")
        return try {
            //jwtTokenHandler.recoverJWT(token)
            val response = carMessageHandler.editCar(body)
            if(response == Response.FAILED.name){
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
