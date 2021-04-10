package com.se.project.repository

import com.se.project.domain.Rental
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [Rental] entity.
 */
@Suppress("unused")
@Repository
interface RentalRepository : JpaRepository<Rental, Long> {
}
