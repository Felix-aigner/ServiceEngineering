package at.serviceengineering.webservice1.dtos

import java.math.BigDecimal


data class ChangeCarRequestDto(
        val id: String,
        val type: String,
        val brand: String,
        val kwPower: Int,
        var usdPrice: BigDecimal,
        var isRented: Boolean
)
