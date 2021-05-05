package at.serviceengineering.microservice.car.controller

import at.serviceengineering.microservice.car.entities.CarRequest
import at.serviceengineering.microservice.car.service.CarService
import at.serviceengineering.microservice.car.service.CurrencyConverterService
import at.serviceengineering.webservice2.wsdl.Currency
import com.google.gson.Gson
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service
import java.util.*

@Service
class Consumer (
        val carService:CarService,
        val currencyConverterService: CurrencyConverterService
){
    @RabbitListener(queues = ["cars.getCars.requests"])
    fun receiveCarRequests(obj: String): String {
        println("Received: '$obj'")

        val request = Gson().fromJson(obj, CarRequest::class.java)

        return if(request.findAll) {
            val list = carService.findAll()
            list.forEach{
                it.price = currencyConverterService.convertCurrency(it.price, Currency.USD, request.currency)
            }
            Gson().toJson(list)
        } else {
            try {
                val car = carService.findOne(request.id!!)
                car.price = currencyConverterService.convertCurrency(car.price, Currency.USD, request.currency)
                Gson().toJson(car)
            } catch (e: Exception) {
                "not-found"
            }
        }
    }
}
