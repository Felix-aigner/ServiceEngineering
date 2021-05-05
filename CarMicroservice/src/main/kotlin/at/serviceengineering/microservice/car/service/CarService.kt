package at.serviceengineering.microservice.car.service

import at.serviceengineering.microservice.car.entities.Car
import at.serviceengineering.microservice.car.repositories.CarRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class CarService(
        private val carRepository: CarRepository,
) {

    fun findAll(currency: Currency): List<Car> = carRepository.findAll()

    fun findOne(id: String): Car = carRepository.findById(id).get()

    fun addCarToDatabase(car: Car) = carRepository.save(car)

    fun deleteCar(id: String) = carRepository.deleteById(id)

    fun isCarRepositoryEmpty(): Boolean = carRepository.findAll().isNullOrEmpty()

    fun changeCar(newCar: Car) : Car {
        val car = findOne(newCar.id)
        car.brand = newCar.brand
        car.type = newCar.type
        car.usdPrice = newCar.usdPrice
        car.kwPower = newCar.kwPower
        return carRepository.save(car)
    }
}

