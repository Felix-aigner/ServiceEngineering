package com.se.project.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

import javax.persistence.*

import java.io.Serializable

/**
 * A Rental.
 */
@Entity
@Table(name = "rental")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
data class Rental(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "start_date")
    var startDate: String? = null,

    @Column(name = "end_date")
    var endDate: String? = null,

    @OneToOne(mappedBy = "rental")
    @JsonIgnore
    var car: Car? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Rental) return false

        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Rental{" +
        "id=$id" +
        ", startDate='$startDate'" +
        ", endDate='$endDate'" +
        "}"


    companion object {
        private const val serialVersionUID = 1L
    }
}
