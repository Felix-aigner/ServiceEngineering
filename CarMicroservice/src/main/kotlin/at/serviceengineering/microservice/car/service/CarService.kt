package at.serviceengineering.microservice.car.service

import at.serviceengineering.microservice.car.entities.Car
import at.serviceengineering.microservice.car.repositories.CarRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class CarService(
        private val carRepository: CarRepository,
) {

    fun findAll(currency: Currency): List<Car> {
        return carRepository.findAll()
    }

    fun findOne(id: String): Car {
        return carRepository.findById(id).get()
    }

    // TODO: move book and return to rentalservice
    /*
    fun bookCar(account: Account, request: RentalDTO) {
        val car = getCar(request.car?.id ?: throw NullPointerException())
        if (car.isRented)
            throw CarAlreadyRentedException()
        car.isRented = true

        val rental = rentalService.save(request)
        account.rentals?.add(rental)

        try {
            carRepository.save(car)
            accountRepository.save(account)
        } catch (e: Exception) {
            account.rentals?.remove(rental)
            accountRepository.save(account)
            throw Exception("Could not update Car, revert Transaction")
        }
    }

    fun returnCar(account: Account, rentalId: UUID) {
        val rental = rentalService.findOneEntity(rentalId)

        if (rental.isActive == false)
            throw InvalidCarStatusManipulationException()
        if (account.rentals?.map { accountRental -> accountRental.id }?.contains(rental.id) == false)
            throw InvalidCarStatusManipulationException()

        val car: Car = getCar(rental.car?.id ?: throw NullPointerException())
        car.isRented = false

        account.rentals?.filter { accountRental -> accountRental.id == rental.id }?.forEach {
            it.isActive = false
        }
        rental.isActive = false
        try {
            rentalService.saveEntity(rental)
            carRepository.save(car)
            accountRepository.save(account)
        } catch (e: Exception) {
            account.rentals?.filter { accountRental -> accountRental.id == rental.id }?.forEach {
                it.isActive = true
            }
            accountRepository.save(account)
            throw Exception("Could not update Car, revert Transaction")
        }
    }
    */

    fun addCarToDatabase(car: Car) {
        carRepository.save(car)
    }

    fun changeCar(newCar: Car) : Car {
        val car = getCar(newCar.id)
        car.brand = newCar.brand
        car.type = newCar.type
        car.usdPrice = newCar.usdPrice
        car.isRented = newCar.isRented
        car.kwPower = newCar.kwPower
        return carRepository.save(car)
    }

    fun deleteCar(id: String) {
        //TODO: change rental objects via communication
        carRepository.deleteById(id)
    }

    private fun getCar(id: String): Car = carRepository.findById(id).get()

    fun isCarRepositoryEmpty(): Boolean = carRepository.findAll().isNullOrEmpty()
}

