package at.serviceengineering.microservice.car

import at.serviceengineering.microservice.car.entities.Car
import at.serviceengineering.microservice.car.service.CarService
import at.serviceengineering.microservice.car.service.CurrencyConverterService
import at.serviceengineering.webservice2.wsdl.Currency
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener
import java.math.BigDecimal
import kotlin.random.Random

@SpringBootApplication
class CarMicroserviceApplication(
		private val carService: CarService
) {
	val logger: Logger = LoggerFactory.getLogger(CarMicroserviceApplication::class.java)

	@EventListener(ApplicationReadyEvent::class)
	fun fillDatabase() {
		try {
			if (carService.isCarRepositoryEmpty()) {
				logger.info("Fill database with cars")
				repeat(10) {
					val price: Double = Random.nextInt(600, 1400).toDouble()
					carService.addCarToDatabase(
							Car(
									id = "",
									brand = "Audi",
									type = "A${Random.nextInt(3, 8)}",
									kwPower = Random.nextInt(120, 250),
									usdPrice = BigDecimal(price)
							)
					)
				}
			}
		} catch (e: Exception) {
			logger.warn("Initial car import failed")
		}
	}
}

fun main(args: Array<String>) {
	runApplication<CarMicroserviceApplication>(*args)
}
