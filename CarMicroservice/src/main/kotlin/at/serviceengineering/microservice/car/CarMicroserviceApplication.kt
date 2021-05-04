package at.serviceengineering.microservice.car

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CarMicroserviceApplication

fun main(args: Array<String>) {
	runApplication<CarMicroserviceApplication>(*args)
}
