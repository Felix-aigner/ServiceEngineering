package at.serviceengineering.microservice.car.entities

import at.serviceengineering.webservice2.wsdl.Currency
import java.math.BigDecimal

data class CarResponse(
        var id: String,

        var type: String,

        var brand: String,

        var kwPower: Int,

        var price: BigDecimal,

        var currency: Currency?
)
