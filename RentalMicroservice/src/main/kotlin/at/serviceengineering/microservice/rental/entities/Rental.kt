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
        var startDate: String? = null,

        @Field("endDate")
        var endDate: String? = null,

        @Field("isActive")
        var isActive: Boolean? = null,

        @Field("carId")
        var carId: String? = null,

        @Field("userId")
        var userId: String? = null,
)
