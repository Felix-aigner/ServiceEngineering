package at.serviceengineering.microservice.rest.service.models


data class Account(

        var id: String,

        var username: String,

        var password: String,

        var firstname: String?,

        var lastname: String?,

        var isAdministrator: Boolean
) {
    fun toAccountResponse(token: String): AccountResponse {
        return AccountResponse(id, username, firstname, lastname, isAdministrator, token)
    }
}

data class AccountResponse(

        var id: String,

        var username: String,

        var firstname: String?,

        var lastname: String?,

        var isAdministrator: Boolean,

        var token: String
)
