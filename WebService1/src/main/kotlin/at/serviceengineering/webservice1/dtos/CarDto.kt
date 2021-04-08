package at.serviceengineering.webservice1.dtos

import at.serviceengineering.webservice1.wsdl.Currency
import java.math.BigDecimal
import java.util.*


class CarDto(
        val id: UUID?,
        val type: String,
        val brand: String,
        val kwPower: Int,
        var price: BigDecimal,
        var currency: Currency,
        var isRented: Boolean
)
