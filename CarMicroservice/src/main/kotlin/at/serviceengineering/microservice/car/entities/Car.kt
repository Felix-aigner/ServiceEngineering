package at.serviceengineering.microservice.car.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
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
)
