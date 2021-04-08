package at.serviceengineering.webservice1.dtos

import java.util.*

data class UserDto (
    val username: String,
    val firstname: String,
    val lastname: String,
    var token: String?,
    var isAdministrator: Boolean,
    var rentedCars: MutableList<UUID>?
)
