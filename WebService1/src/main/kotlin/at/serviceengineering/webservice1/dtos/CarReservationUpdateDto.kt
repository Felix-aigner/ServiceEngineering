package at.serviceengineering.webservice1.dtos

data class CarReservationUpdateDto(
        var carId: String,
        var startDate: String,
        var endDate: String
)
