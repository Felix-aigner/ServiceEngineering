package at.serviceengineering.webservice1.dtos

import at.serviceengineering.webservice1.enums.Currency

data class CarReservationUpdateDto(
        var id: String,
        var currency: Currency
)
