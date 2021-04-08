package at.serviceengineering.webservice1

import at.serviceengineering.webservice1.dtos.AccountCreationDto
import at.serviceengineering.webservice1.entities.Car
import at.serviceengineering.webservice1.enums.Currency
import at.serviceengineering.webservice1.services.AccountService
import at.serviceengineering.webservice1.services.CarService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener
import kotlin.random.Random


@SpringBootApplication
class WebService1Application(
		private val carService: CarService,
		private val accountService: AccountService
) {
	val logger: Logger = LoggerFactory.getLogger(WebService1Application::class.java)

	@EventListener(ApplicationReadyEvent::class)
	fun fillDatabase() {
		try {
			if(carService.findAll(currency = Currency.USD).isEmpty()) {
				logger.info("Fill database with cars")
				repeat(10) {
					carService.addCarToDatabase(
							Car(
									id = null,
									brand = "Audi",
									type = "A${Random.nextInt(3, 8)}",
									kwPower = Random.nextInt(120, 250),
									usdPrice = Random.nextFloat()+1000,
									isRented = false
							)
					)
				}
			}
		} catch (e: Exception) {
			logger.warn("Initial car import failed")
		}
		try {
			if(accountService.findAll().isEmpty()) {
				logger.info("Add Administrator")
				accountService.createAdministrator(
						AccountCreationDto(
								username = "admin",
								password = "admin",
								firstname = "firstname",
								lastname = "lastname"
						)
				)
			}
		} catch (e: Exception) {
			logger.warn("Initial car import failed")
		}
	}
}

fun main(args: Array<String>) {
	runApplication<WebService1Application>(*args)
}
