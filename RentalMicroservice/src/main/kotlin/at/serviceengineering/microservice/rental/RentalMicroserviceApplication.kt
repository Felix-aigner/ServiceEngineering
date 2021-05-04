package at.serviceengineering.microservice.rental

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RentalMicroserviceApplication

fun main(args: Array<String>) {
	runApplication<RentalMicroserviceApplication>(*args)
}
