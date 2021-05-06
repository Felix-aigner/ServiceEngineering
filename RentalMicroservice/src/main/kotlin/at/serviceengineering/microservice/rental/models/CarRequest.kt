package at.serviceengineering.microservice.rental.models


data class CarRequest(
        val id: String?,
        val currency: String,
        val findAll: Boolean
)
