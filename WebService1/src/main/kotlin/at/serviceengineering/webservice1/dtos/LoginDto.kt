package at.serviceengineering.webservice1.dtos

import com.google.gson.Gson

data class LoginDto(
    val username: String,
    val password: String
) {
    fun toJson(): String {
        return Gson().toJson(this)
    }
}
