package at.serviceengineering.webservice1.services

import at.serviceengineering.webservice1.dtos.CarReservationUpdateDto
import at.serviceengineering.webservice1.entities.Account
import at.serviceengineering.webservice1.entities.Car
import at.serviceengineering.webservice1.repositories.ICarRepository
import org.springframework.stereotype.Service

@Service
class CarService(
        private val carRepository: ICarRepository
): ICarService {

    override fun findAll(): List<Car> = carRepository.findAll()

    override fun bookCar(account: Account, request: CarReservationUpdateDto) {
        TODO("Not yet implemented")
    }

    override fun returnCar(account: Account, request: CarReservationUpdateDto) {
        TODO("Not yet implemented")
    }

    fun addCarToDatabase(car: Car) {
        carRepository.save(car)
    }
}
