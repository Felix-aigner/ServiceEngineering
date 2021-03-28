package at.serviceengineering.webservice1.services

import at.serviceengineering.webservice1.dtos.CarReservationUpdateDto
import at.serviceengineering.webservice1.entities.Account
import at.serviceengineering.webservice1.entities.Car

interface ICarService {

    fun findAll(): List<Car>
    fun bookCar(account: Account, request: CarReservationUpdateDto)
    fun returnCar(account: Account, request: CarReservationUpdateDto)
}
