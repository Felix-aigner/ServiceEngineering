package at.serviceengineering.webservice1.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.hibernate.annotations.GenericGenerator

import javax.persistence.*

import java.io.Serializable
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

        @OneToOne(mappedBy = "rental")
        var car: Car
)
