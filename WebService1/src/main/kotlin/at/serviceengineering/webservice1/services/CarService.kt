package at.serviceengineering.webservice1.services

import at.serviceengineering.webservice1.dtos.CarDto
import at.serviceengineering.webservice1.dtos.CarReservationUpdateDto
import at.serviceengineering.webservice1.dtos.ChangeCarRequestDto
import at.serviceengineering.webservice1.dtos.RentalDTO
import at.serviceengineering.webservice1.entities.Account
import at.serviceengineering.webservice1.entities.Car
import at.serviceengineering.webservice1.exceptions.CarAlreadyRentedException
import at.serviceengineering.webservice1.exceptions.CarNotFoundException
import at.serviceengineering.webservice1.exceptions.InvalidCarStatusManipulationException
import at.serviceengineering.webservice1.mapper.CarMapper
import at.serviceengineering.webservice1.repositories.IAccountRepository
import at.serviceengineering.webservice1.repositories.ICarRepository
import at.serviceengineering.webservice1.wsdl.Currency
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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

    override fun findOne(id: UUID, currency: Currency): CarDto {
        return carRepository.findById(id).map{ car: Car -> carMapper.mapToCarDtoWithCustomCurrency(car, currency)}.get()
    }

    override fun bookCar(account: Account, request: CarReservationUpdateDto) {
        val car = getCar(request.id)
        if(car.isRented)
            throw CarAlreadyRentedException()

        car.isRented = true
        account.rentedCars?.add(car.id?: throw NullPointerException())
        accountRepository.save(account)

        try {
            carRepository.save(car)
        } catch (e: Exception) {
            account.rentedCars?.remove(car.id?: throw NullPointerException())
            accountRepository.save(account)
            throw Exception("Could not update Car, revert Transaction")
        }
    }

    override fun returnCar(account: Account, request: CarReservationUpdateDto) {
        if(account.rentedCars?.contains(UUID.fromString(request.id)) == false)
            throw InvalidCarStatusManipulationException()

        val car = getCar(request.id)
        account.rentedCars?.remove(car.id?: throw NullPointerException())
        car.isRented = false
        accountRepository.save(account)

        try {
            carRepository.save(car)
        } catch (e: Exception) {
            account.rentedCars?.add(car.id?: throw NullPointerException())
            accountRepository.save(account)
            throw Exception("Could not update Car, revert Transaction")
        }
    }

    override fun addCarToDatabase(car: Car) {
        carRepository.save(car)
    }

    override fun changeCar(newCar: ChangeCarRequestDto) {
        val car = getCar(newCar.id)
        car.brand = newCar.brand
        car.type = newCar.type
        car.usdPrice = newCar.usdPrice
        car.isRented = newCar.isRented
        car.kwPower = newCar.kwPower
        carRepository.save(car)
    }

    override fun deleteCar(id: UUID): Unit {
        carRepository.deleteById(id)
    }

    private fun getCar(id: String): Car = carRepository.findCarById(UUID.fromString(id))?: throw CarNotFoundException()

    fun isCarRepositoryEmpty(): Boolean = carRepository.findAll().isNullOrEmpty()
}
