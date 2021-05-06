package at.serviceengineering.microservice.user.controller

import at.serviceengineering.microservice.user.entities.Account
import at.serviceengineering.microservice.user.service.AccountService
import com.google.gson.Gson
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class Consumer (
        val accountService: AccountService
        ){
    @RabbitListener(queues = ["user.getName.requests"])
    fun receive(obj: String): String {

        println("Received: '$obj'")
        return "Hans"
    }

    @RabbitListener(queues = ["user.getAccounts.requests"])
    fun receiveAccountsRequests(obj: String): String {
        println("Received: '$obj'")
        if(obj.toLowerCase().equals("getallaccounts")) {
            return Gson().toJson(accountService.findAll())
        }
        return Gson().toJson(accountService.findOne(obj))
    }

    @RabbitListener(queues = ["user.createAccount.requests"])
    fun receiveCreateAccountRequests(obj: String): String {
        println("Received: '$obj'")
        return try {
            val account = Gson().fromJson(obj, Account::class.java)
            Gson().toJson(accountService.createAccount(account))
        }catch (e : Exception){
            "username already exists"
        }
    }

    @RabbitListener(queues = ["user.login.requests"])
    fun receiveLoginRequests(obj: String): String {
        println("Received: '$obj'")
        return try {
            val account = Gson().fromJson(obj, Account::class.java)
            Gson().toJson(accountService.login(account).id)
        }catch (e : Exception){
            "invalid credentials"
        }
    }

    @RabbitListener(queues = ["user.deleteAccount.requests"])
    fun receiveDeleteAccountRequests(obj: String): String {
        println("Received: '$obj'")
        return try {
            accountService.deleteAccount(obj)
            "done"
        }catch (e : Exception){
            "failed"
        }
    }
}
