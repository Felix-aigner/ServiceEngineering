package at.serviceengineering.webservice1.services

import at.serviceengineering.webservice1.dtos.CarDto
import at.serviceengineering.webservice1.dtos.CarReservationUpdateDto
import at.serviceengineering.webservice1.entities.Account
import at.serviceengineering.webservice1.entities.Car
import at.serviceengineering.webservice1.enums.Currency
import at.serviceengineering.webservice1.mapper.CarMapper
import at.serviceengineering.webservice1.repositories.ICarRepository
import org.springframework.stereotype.Service

@Service
class CarService(
        private val carRepository: ICarRepository,
        private val carMapper: CarMapper
): ICarService {

    override fun findAll(currency: Currency): List<CarDto> = carRepository.findAll().map{
        car: Car ->  carMapper.mapToCarDtoWithCustomCurrency(car, currency)
    }

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
