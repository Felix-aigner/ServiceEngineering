package at.serviceengineering.webservice1.dtos

import at.serviceengineering.webservice1.enums.Currency
import java.util.*


class CarDto(
        val id: UUID?,
        val type: String,
        val brand: String,
        val kwPower: Int,
        var price: Float,
        var currency: Currency,
        var isRented: Boolean
)
