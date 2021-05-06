package at.serviceengineering.microservice.car.service

import at.serviceengineering.microservice.car.entities.Car
import at.serviceengineering.microservice.car.repositories.CarRepository
import org.springframework.stereotype.Service

@Service
class CarService(
        private val carRepository: CarRepository,
) {

    fun findAll(): List<Car> = carRepository.findAll()

    fun findOne(id: String): Car = carRepository.findById(id).get()

    fun addCar(car: Car) = carRepository.save(car)

    fun isCarRepositoryEmpty(): Boolean = carRepository.findAll().isNullOrEmpty()

    fun deleteCar(id: String) = carRepository.deleteById(id)

    fun changeCar(newCar: Car) : Car {
        val car = findOne(newCar.id)
        car.brand = newCar.brand
        car.type = newCar.type
        car.price = newCar.price
        car.kwPower = newCar.kwPower
        return carRepository.save(car)
    }
}

