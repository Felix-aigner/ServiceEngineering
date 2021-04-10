package at.serviceengineering.webservice1.entities


import com.sun.istack.Nullable
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.hibernate.annotations.GenericGenerator

import javax.persistence.*

import java.util.*

/**
 * A Rental.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
data class Rental(
        @Id
        @GeneratedValue(generator = "UUID")
        @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
        var id: UUID?,

        @Column
        var startDate: String? = null,

        @Column
        var endDate: String? = null,

        @Column
        var isActive: Boolean? = null,

        @OneToOne
        var car: Car? = null
)
