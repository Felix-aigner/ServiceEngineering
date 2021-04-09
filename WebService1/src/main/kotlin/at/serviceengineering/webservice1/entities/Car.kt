package at.serviceengineering.webservice1.entities

import org.hibernate.annotations.GenericGenerator
import java.math.BigDecimal
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Car(
        @Id
        @GeneratedValue(generator = "UUID")
        @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
        var id: UUID?,

        @Column(nullable = false)
        var type: String,

        @Column(nullable = false)
        var brand: String,

        @Column(nullable = false)
        var kwPower: Int,

        @Column(nullable = false)
        var usdPrice: BigDecimal,

        @Column(nullable = false)
        var isRented: Boolean

)
