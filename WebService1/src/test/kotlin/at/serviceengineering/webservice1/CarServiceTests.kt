package at.serviceengineering.webservice1

import at.serviceengineering.webservice1.entities.Car
import at.serviceengineering.webservice1.services.AccountService
import at.serviceengineering.webservice1.services.CarService
import at.serviceengineering.webservice1.wsdl.Currency
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class CarServiceTests (
        @Autowired val carService: CarService
) {
    @Test
    fun addCar() {
        val car = Car(
                id = UUID.randomUUID(),
                type = "TestType",
                brand = "TestBrand",
                kwPower = 100,
                usdPrice = 100.toBigDecimal(),
                isRented = false
        )

        carService.addCarToDatabase(car);
        val id = carService.findOne(car.id!!, Currency.USD).id;
        assert(id == car.id)
    }

    @Test
    fun findCar() {
        val car = Car(
                id = UUID.randomUUID(),
                type = "TestType",
                brand = "TestBrand",
                kwPower = 100,
                usdPrice = 100.toBigDecimal(),
                isRented = false
        )

        carService.addCarToDatabase(car);
        val id = carService.findOne(car.id!!, Currency.USD).id;
        assert(id == car.id)
    }
}