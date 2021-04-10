package at.serviceengineering.webservice1.dtos

import at.serviceengineering.webservice1.entities.Rental
import java.util.*

data class AccountDto(
        val id: UUID,
        val username: String,
        val firstname: String,
        val lastname: String,
        var token: String?,
        var isAdministrator: Boolean,
        var rentals: MutableList<Rental>?
)
