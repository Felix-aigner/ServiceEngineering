package at.serviceengineering.microservice.user.controller

import at.serviceengineering.microservice.user.entities.Account
import at.serviceengineering.microservice.user.service.AccountService
import at.serviceengineering.microservice.user.utils.Response
import com.google.gson.Gson
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class Consumer (
        val accountService: AccountService
        ){
    @RabbitListener(queues = ["user.getAccounts.requests"])
    fun receiveAccountsRequests(obj: String): String {
        println("Received: '$obj'")
        return try {
            if(obj.toLowerCase().equals("getallaccounts")) {
                Gson().toJson(accountService.findAll())
            }
            else Gson().toJson(accountService.findOne(obj))
        }catch (e : Exception){
            Response.FAILED.name
        }
    }

    @RabbitListener(queues = ["user.createAccount.requests"])
    fun receiveCreateAccountRequests(obj: String): String {
        println("Received: '$obj'")
        return try {
            val account = Gson().fromJson(obj, Account::class.java)
            Gson().toJson(accountService.createAccount(account))
        }catch (e : Exception){
            Response.FAILED.name
        }
    }

    @RabbitListener(queues = ["user.login.requests"])
    fun receiveLoginRequests(obj: String): String {
        println("Received: '$obj'")
        return try {
            val account = Gson().fromJson(obj, Account::class.java)
            Gson().toJson(accountService.login(account).id)
        }catch (e : Exception){
            Response.FAILED.name
        }
    }

    @RabbitListener(queues = ["user.deleteAccount.requests"])
    fun receiveDeleteAccountRequests(obj: String): String {
        println("Received: '$obj'")
        return try {
            accountService.deleteAccount(obj)
            Response.OK.name
        }catch (e : Exception){
            Response.FAILED.name
        }
    }

    @RabbitListener(queues = ["user.isAccount.requests"])
    fun receiveIsAccountRequests(obj: String): String {
        println("Received: '$obj'")
        return try {
            accountService.findOne(obj)
            Response.OK.name
        }catch (e : Exception){
            Response.FAILED.name
        }
    }

}
