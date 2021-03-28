package at.serviceengineering.webservice1.utils

import java.util.*

object Unwrap {

    fun <Car> Optional<Car>.unwrapCar(): Car? = orElse(null)
}
