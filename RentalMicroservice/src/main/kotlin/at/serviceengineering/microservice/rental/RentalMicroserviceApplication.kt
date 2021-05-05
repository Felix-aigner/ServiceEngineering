package at.serviceengineering.microservice.rental

import at.serviceengineering.microservice.rental.controller.Producer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener

@SpringBootApplication
class RentalMicroserviceApplication(
		val producer: Producer
) {
	@EventListener(ApplicationReadyEvent::class)
	fun start() {
		producer.send()
	}
}

fun main(args: Array<String>) {
	runApplication<RentalMicroserviceApplication>(*args)
}
