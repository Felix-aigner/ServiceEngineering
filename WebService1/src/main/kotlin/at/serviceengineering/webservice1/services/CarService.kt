package at.serviceengineering.webservice1.services

import at.serviceengineering.webservice1.entities.Car
import at.serviceengineering.webservice1.repositories.ICarRepository
import org.springframework.stereotype.Service

@Service
class CarService(
        private val carRepository: ICarRepository
): ICarService {

    override fun findAll(): List<Car> = carRepository.findAll()
}
