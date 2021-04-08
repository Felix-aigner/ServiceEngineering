package at.serviceengineering.webservice1.services

import at.serviceengineering.webservice1.dtos.CarDto
import at.serviceengineering.webservice1.dtos.CarReservationUpdateDto
import at.serviceengineering.webservice1.dtos.ChangeCarRequestDto
import at.serviceengineering.webservice1.entities.Account
import at.serviceengineering.webservice1.entities.Car
import at.serviceengineering.webservice1.enums.Currency

interface ICarService {

    fun findAll(currency: Currency): List<CarDto>
    fun bookCar(account: Account, request: CarReservationUpdateDto)
    fun returnCar(account: Account, request: CarReservationUpdateDto)
    fun addCarToDatabase(car: Car)
    fun changeCar(car: ChangeCarRequestDto)
}
