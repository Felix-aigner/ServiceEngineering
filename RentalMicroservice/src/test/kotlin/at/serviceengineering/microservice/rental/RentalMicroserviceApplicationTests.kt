package at.serviceengineering.microservice.rental

import at.serviceengineering.microservice.rental.entities.Rental
import at.serviceengineering.microservice.rental.services.RentalService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RentalMicroserviceApplicationTests {

	@Autowired
	val rentalService: RentalService? = null

	val rental = Rental("testId", "testStartDate", "testEndDate", true, "testCarId", "testUserId")
	val rental2 = Rental("testId2", "testStartDate2", "testEndDate2", true, "testCarId2", "testUserId2")
	val rental3 = Rental("testId3", "testStartDate3", "testEndDate3", true, "testCarId3", "testUserId3")
	val rental4 = Rental("testId4", "testStartDate4", "testEndDate4", true, "testCarId2", "testUserId4")

	@BeforeEach
	fun setup() {
		cleanUp()
	}

	@AfterEach
	fun teardown() {
		cleanUp()
	}

	fun cleanUp() {
		var rentals = rentalService?.findAll()

		if (rentals != null) {
			for (rental in rentals) {
				rentalService?.delete(rental.id!!)
			}

			rentals = rentalService?.findAll()
		}

		assert(rentals!!.isEmpty())
	}

	@Test
	fun saveRental() {
		rentalService?.save(rental)

		val rentals = rentalService?.findAll()
		assert(rentals!!.contains(rental))
	}

	@Test
	fun findAll() {
		rentalService?.save(rental)
		rentalService?.save(rental2)
		rentalService?.save(rental3)

		val rentals = rentalService?.findAll()

		assert(rentals!!.contains(rental))
		assert(rentals.contains(rental2))
		assert(rentals.contains(rental3))
	}

	@Test
	fun findOne() {
		rentalService?.save(rental)

		assert(rentalService?.findOne(rental.id!!) != null)
	}

	@Test
	fun findAllByCarId() {
		rentalService?.save(rental)
		rentalService?.save(rental2)
		rentalService?.save(rental3)
		rentalService?.save(rental4)

		val rentals = rentalService?.findAllByCarId(rental2.carId)

		assert(!rentals!!.contains(rental))
		assert(rentals.contains(rental2))
		assert(!rentals.contains(rental3))
		assert(rentals.contains(rental4))
	}

	@Test
	fun delete() {
		rentalService?.save(rental)

		assert(rentalService?.findOne(rental.id!!) != null)

		rentalService?.delete(rental.id!!)

		val rentals = rentalService?.findAll()
		assert(!rentals!!.contains(rental))
	}
}
