package at.serviceengineering.webservice1.services

import at.serviceengineering.webservice1.entities.Car

interface ICarService {

    fun findAll(): List<Car>
}
