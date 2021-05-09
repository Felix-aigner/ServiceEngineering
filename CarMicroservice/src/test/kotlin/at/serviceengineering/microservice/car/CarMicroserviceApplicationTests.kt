package at.serviceengineering.microservice.car

import at.serviceengineering.microservice.car.entities.Car
import at.serviceengineering.microservice.car.service.CarService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

@SpringBootTest
class CarMicroserviceApplicationTests {

	@Autowired
	val carService: CarService? = null

	val car = Car("testId","testType","testBrand",999, BigDecimal.ONE)
	val car2 = Car("testId2","testType2","testBrand2",2999, BigDecimal.valueOf(2))
	val car3 = Car("testId3","testType3","testBrand3",3999, BigDecimal.valueOf(3))

	@BeforeEach
	fun setup() {
		cleanUp()
	}

	@AfterEach
	fun teardown() {
		cleanUp()
	}

	fun cleanUp() {
		val cars = carService?.findAll()

		if (cars != null) {
			for (car in cars) {
				carService?.deleteCar(car.id)
			}
		}

		assert(carService!!.isCarRepositoryEmpty())
	}

	@Test
	fun addCar() {
		carService?.addCar(car)

		assert(!carService!!.isCarRepositoryEmpty())
	}

	@Test
	fun findOne() {
		carService?.addCar(car)

		assert(carService?.findOne(car.id) != null)
	}

	@Test
	fun findAll() {
		carService?.addCar(car)
		carService?.addCar(car2)
		carService?.addCar(car3)

		val cars = carService?.findAll()
		assert(cars!!.contains(car))
		assert(cars.contains(car2))
		assert(cars.contains(car3))
	}

	@Test
	fun isCarRepositoryEmpty() {
		assert(carService!!.isCarRepositoryEmpty())
		carService?.addCar(car)
		assert(!carService!!.isCarRepositoryEmpty())
		carService?.deleteCar(car.id)
		assert(carService!!.isCarRepositoryEmpty())
	}

	@Test
	fun deleteCar() {
		carService?.addCar(car)

		var cars = carService?.findAll()
		assert(cars!!.contains(car))

		carService?.deleteCar(car.id)

		cars = carService?.findAll()
		assert(!cars!!.contains(car))
	}
}
