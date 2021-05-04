package at.serviceengineering.microservice.user.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field


@Document(collection = "Account")
data class Account (

    @Id
    var id: String?,

    @Field("username")
    var username: String,

    @Field("password")
    var password: String,

    @Field("firstname")
    var firstname: String,

    @Field("lastname")
    var lastname: String,

    @Field("isAdministrator")
    var isAdministrator: Boolean,
)
