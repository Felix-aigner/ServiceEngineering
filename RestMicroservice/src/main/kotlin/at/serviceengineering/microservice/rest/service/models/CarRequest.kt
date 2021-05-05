package at.serviceengineering.microservice.rest.service.models


data class CarRequest(
        val id: String?,
        val currency: String,
        val findAll: Boolean
)
