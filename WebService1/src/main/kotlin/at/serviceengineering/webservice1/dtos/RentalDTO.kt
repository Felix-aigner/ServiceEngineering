package at.serviceengineering.webservice1.dtos

import at.serviceengineering.webservice1.entities.Car
import java.io.Serializable
import java.util.*


data class RentalDTO(

        var id: UUID?,
        var startDate: String? = null,
        var endDate: String? = null,
        var isActive: Boolean? = null,
        var car: CarDto

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RentalDTO) return false
        return id!= null && id == other.id
    }

    override fun hashCode() = 31
}
