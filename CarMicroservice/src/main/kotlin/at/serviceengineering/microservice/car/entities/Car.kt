package at.serviceengineering.microservice.car.entities

import at.serviceengineering.microservice.car.exceptions.SoapCallException
import at.serviceengineering.microservice.car.service.CurrencyConverterService
import at.serviceengineering.webservice2.wsdl.Currency
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.stereotype.Service
import java.math.BigDecimal


@Document(collection = "Car")
data class Car(
        @Id
        var id: String,

        @Field("type")
        var type: String,

        @Field("brand")
        var brand: String,

        @Field("kwPower")
        var kwPower: Int,

        @Field("usdPrice")
        var price: BigDecimal
) {

    fun toCarResponse(): CarResponse {
        return CarResponse(id, type, brand, kwPower, price, null)
    }
}
