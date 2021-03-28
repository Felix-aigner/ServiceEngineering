package at.serviceengineering.webservice1.services

import at.serviceengineering.webservice1.dtos.CarDto
import at.serviceengineering.webservice1.dtos.CarReservationUpdateDto
import at.serviceengineering.webservice1.entities.Account
import at.serviceengineering.webservice1.entities.Car
import at.serviceengineering.webservice1.enums.Currency
import at.serviceengineering.webservice1.exceptions.CarNotFoundException
import at.serviceengineering.webservice1.mapper.CarMapper
import at.serviceengineering.webservice1.repositories.IAccountRepository
import at.serviceengineering.webservice1.repositories.ICarRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class CarService(
        private val carRepository: ICarRepository,
        private val accountRepository: IAccountRepository,
        private val carMapper: CarMapper
): ICarService {

    override fun findAll(currency: Currency): List<CarDto> = carRepository.findAll().map{
        car: Car ->  carMapper.mapToCarDtoWithCustomCurrency(car, currency)
    }

    override fun bookCar(account: Account, request: CarReservationUpdateDto) {
        val car = getCar(request.id)
        car.isRented = true
        account.rentedCars?.add(car)
        accountRepository.save(account)

        try {
            carRepository.save(car)
        } catch (e: Exception) {
            account.rentedCars?.remove(car)
            accountRepository.save(account)
            throw Exception("Could not update Car, revert Transaction")
        }
    }

    override fun returnCar(account: Account, request: CarReservationUpdateDto) {
        val car = getCar(request.id)
        account.rentedCars?.remove(car)
        car.isRented = false
        accountRepository.save(account)

        try {
            carRepository.save(car)
        } catch (e: Exception) {
            account.rentedCars?.add(car)
            accountRepository.save(account)
            throw Exception("Could not update Car, revert Transaction")
        }
    }

    fun addCarToDatabase(car: Car) {
        carRepository.save(car)
    }

    fun getCar(id: String): Car = carRepository.findCarById(UUID.fromString(id))?: throw CarNotFoundException()

}
