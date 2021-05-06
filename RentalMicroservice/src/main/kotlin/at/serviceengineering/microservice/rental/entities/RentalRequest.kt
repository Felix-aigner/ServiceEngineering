package at.serviceengineering.microservice.rental.entities

data class RentalRequest(
        val rental: Rental?,
        val id: String?,
        val method: String
)
