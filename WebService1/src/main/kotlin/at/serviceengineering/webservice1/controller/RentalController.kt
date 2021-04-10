package com.se.project.web.rest

import com.se.project.service.RentalService
import com.se.project.web.rest.errors.BadRequestAlertException
import com.se.project.service.dto.RentalDTO

import io.github.jhipster.web.util.HeaderUtil
import io.github.jhipster.web.util.ResponseUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import java.net.URI
import java.net.URISyntaxException

private const val ENTITY_NAME = "rental"
/**
 * REST controller for managing [com.se.project.domain.Rental].
 */
@RestController
@RequestMapping("/api")
class RentalResource(
    private val rentalService: RentalService
) {

    private val log = LoggerFactory.getLogger(javaClass)
    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /rentals` : Create a new rental.
     *
     * @param rentalDTO the rentalDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new rentalDTO, or with status `400 (Bad Request)` if the rental has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rentals")
    fun createRental(@RequestBody rentalDTO: RentalDTO): ResponseEntity<RentalDTO> {
        log.debug("REST request to save Rental : $rentalDTO")
        if (rentalDTO.id != null) {
            throw BadRequestAlertException(
                "A new rental cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = rentalService.save(rentalDTO)
        return ResponseEntity.created(URI("/api/rentals/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()))
            .body(result)
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
    @PutMapping("/rentals")
    fun updateRental(@RequestBody rentalDTO: RentalDTO): ResponseEntity<RentalDTO> {
        log.debug("REST request to update Rental : $rentalDTO")
        if (rentalDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = rentalService.save(rentalDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, false, ENTITY_NAME,
                     rentalDTO.id.toString()
                )
            )
            .body(result)
    }
    /**
     * `GET  /rentals` : get all the rentals.
     *

     * @param filter the filter of the request.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of rentals in body.
     */
    @GetMapping("/rentals")    
    fun getAllRentals(@RequestParam(required = false) filter: String?): MutableList<RentalDTO> {
        if ("car-is-null".equals(filter)) {
            log.debug("REST request to get all Rentals where car is null")
            return rentalService.findAllWhereCarIsNull()
        }
        log.debug("REST request to get all Rentals")
        
        return rentalService.findAll()
            }

    /**
     * `GET  /rentals/:id` : get the "id" rental.
     *
     * @param id the id of the rentalDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the rentalDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/rentals/{id}")
    fun getRental(@PathVariable id: Long): ResponseEntity<RentalDTO> {
        log.debug("REST request to get Rental : $id")
        val rentalDTO = rentalService.findOne(id)
        return ResponseUtil.wrapOrNotFound(rentalDTO)
    }
    /**
     *  `DELETE  /rentals/:id` : delete the "id" rental.
     *
     * @param id the id of the rentalDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/rentals/{id}")
    fun deleteRental(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete Rental : $id")

        rentalService.delete(id)
            return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build()
    }
}
