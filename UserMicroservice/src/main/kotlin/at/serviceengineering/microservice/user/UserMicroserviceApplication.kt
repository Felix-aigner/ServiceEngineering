package at.serviceengineering.microservice.user

import at.serviceengineering.microservice.user.entities.Account
import at.serviceengineering.microservice.user.service.AccountService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener

@SpringBootApplication
class UserMicroserviceApplication(
		val accountService: AccountService
) {
	val logger: Logger = LoggerFactory.getLogger(UserMicroserviceApplication::class.java)

	@EventListener(ApplicationReadyEvent::class)
	fun fillDatabase() {
		try {
			if (accountService.findAll().isEmpty()) {
				logger.info("Add Administrator")
				accountService.createAdministrator(
						Account(
								id= "",
								username = "admin",
								password = "admin",
								firstname = "firstname",
								lastname = "lastname",
								isAdministrator = true
						)
				)
			}
		} catch (e: Exception) {
			println(e.localizedMessage)
			println(e.message)
			logger.warn("Initial Admin import failed")
		}
	}
}

fun main(args: Array<String>) {
	runApplication<UserMicroserviceApplication>(*args)
}
