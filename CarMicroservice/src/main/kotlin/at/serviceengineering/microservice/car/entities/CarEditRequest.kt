package at.serviceengineering.microservice.car.entities

data class CarEditRequest(
        val car: Car?,
        val id: String?,
        val method: String
)
