package at.serviceengineering.webservice1.dtos


data class ChangeCarRequestDto(
        val id: String,
        val type: String,
        val brand: String,
        val kwPower: Int,
        var usdPrice: Float,
        var isRented: Boolean
)
