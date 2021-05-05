package at.serviceengineering.microservice.car.entities

import at.serviceengineering.webservice2.wsdl.Currency

data class CarRequest(
        val id: String?,
        val currency: Currency,
        val findAll: Boolean
)
