package at.serviceengineering.webservice1.services

import at.serviceengineering.webservice1.dtos.CarDto
import at.serviceengineering.webservice1.dtos.CarReservationUpdateDto
import at.serviceengineering.webservice1.dtos.ChangeCarRequestDto
import at.serviceengineering.webservice1.dtos.RentalDTO
import at.serviceengineering.webservice1.entities.Account
import at.serviceengineering.webservice1.entities.Car
import at.serviceengineering.webservice1.entities.Rental
import at.serviceengineering.webservice1.wsdl.Currency
import java.util.*

interface ICarService {

    fun findAll(currency: Currency): List<CarDto>
    fun findOne(id: UUID, currency: Currency) : CarDto
    fun bookCar(account: Account, request: RentalDTO)
    fun returnCar(account: Account, request: UUID)
    fun addCarToDatabase(car: Car)
    fun changeCar(car: ChangeCarRequestDto)
    fun deleteCar(id: UUID)
}
