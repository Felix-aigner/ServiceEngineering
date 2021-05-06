package at.serviceengineering.microservice.rental.entities


import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

/**
 * A Rental.
 */
@Document(collection = "Rental")
data class Rental(
        @Id
        var id: String?,

        @Field("startDate")
        var startDate: String,

        @Field("endDate")
        var endDate: String,

        @Field("isActive")
        var isActive: Boolean,

        @Field("carId")
        var carId: String,

        @Field("userId")
        var userId: String,
)
