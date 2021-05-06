package at.serviceengineering.microservice.car.controller

import at.serviceengineering.microservice.car.CarMicroserviceApplication
import at.serviceengineering.microservice.car.entities.Car
import at.serviceengineering.microservice.car.entities.CarEditRequest
import at.serviceengineering.microservice.car.entities.CarRequest
import at.serviceengineering.microservice.car.exceptions.SoapCallException
import at.serviceengineering.microservice.car.service.CarService
import at.serviceengineering.microservice.car.service.CurrencyConverterService
import at.serviceengineering.microservice.car.utils.Response
import at.serviceengineering.webservice2.wsdl.Currency
import com.google.gson.Gson
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class CarRabbitConsumer(
        val carService: CarService,
        val currencyConverterService: CurrencyConverterService
) {
    val logger: Logger = LoggerFactory.getLogger(CarRabbitConsumer::class.java)

    @RabbitListener(queues = ["cars.getCars.requests"])
    fun receiveCarRequests(obj: String): String {
        logger.info(" [X] Received Get Cars: '$obj'")

        val request = Gson().fromJson(obj, CarRequest::class.java)

        return try {
            if (request.findAll) {
                val list = carService.findAll().map { car -> car.toCarResponse() }
                list.forEach {
                    it.price = try {
                        it.currency = request.currency
                        currencyConverterService.convertCurrency(it.price, Currency.USD, request.currency)
                    } catch (e: Exception){
                        it.currency = Currency.USD
                        it.price
                    }
                }
                Gson().toJson(list)
            } else {
                val car = carService.findOne(request.id!!).toCarResponse()
                car.price = try {
                    car.currency = request.currency
                    currencyConverterService.convertCurrency(car.price, Currency.USD, request.currency)
                } catch (e: Exception){
                    car.currency = Currency.USD
                    car.price
                }
                Gson().toJson(car)
            }
        } catch (e: Exception) {
            logger.error(" [-] Failed Get Cars: '$obj' ${e.message}")
            Response.FAILED.name
        }
    }

    @RabbitListener(queues = ["cars.editCar.requests"])
    fun receiveEditCarRequests(obj: String): String {
        logger.info(" [X] Received Edit Car: '$obj'")
        try {
            val request = Gson().fromJson(obj, CarEditRequest::class.java)
            when (request.method) {
                "post" -> carService.addCar(request.car ?: throw Exception())
                "put" -> carService.changeCar(request.car ?: throw Exception())
                "delete" -> carService.deleteCar(request.id ?: throw Exception())
                else -> throw Exception()
            }
            return Response.OK.name
        } catch (e: Exception) {
            logger.error(" [-] Failed Edit Car: '$obj'")
            return Response.FAILED.name
        }
    }
}
