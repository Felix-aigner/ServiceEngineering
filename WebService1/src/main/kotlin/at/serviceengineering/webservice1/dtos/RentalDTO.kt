package com.se.project.service.dto

import java.io.Serializable

/**
 * A DTO for the [com.se.project.domain.Rental] entity.
 */
data class RentalDTO(
    
    var id: Long? = null,

    var startDate: String? = null,

    var endDate: String? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RentalDTO) return false
        return id!= null && id == other.id
    }

    override fun hashCode() = 31
}
