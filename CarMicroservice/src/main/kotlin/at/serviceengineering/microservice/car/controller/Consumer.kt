package at.serviceengineering.microservice.car.controller

import at.serviceengineering.microservice.car.service.CarService
import com.google.gson.Gson
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service
import java.util.*

@Service
class Consumer (
        val carService:CarService
){
    @RabbitListener(queues = ["cars.getCars.requests"])
    fun receiveCarRequests(obj: String): String {

        println("Received: '$obj'")

        if(obj == "findAll") {
            return Gson().toJson(carService.findAll(Currency.getInstance(Locale.GERMAN)))//locale temp
        }

        return "one"
        //return Gson().toJson(carService.findOne(obj))//läuft hier in infinite loop, kA why..
    }
}