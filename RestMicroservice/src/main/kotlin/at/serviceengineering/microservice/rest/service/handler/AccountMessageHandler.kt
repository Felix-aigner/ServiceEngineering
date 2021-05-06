package at.serviceengineering.microservice.rest.service.handler

import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AccountMessageHandler {

    @Autowired
    private val template: RabbitTemplate? = null

    @Autowired
    private val nameExchange: DirectExchange? = null
    @Autowired
    private val accountsExchange: DirectExchange? = null
    @Autowired
    private val accountCreationExchange: DirectExchange? = null
    @Autowired
    private val loginExchange: DirectExchange? = null
    @Autowired
    private val deleteExchange: DirectExchange? = null


    fun send(exchange: DirectExchange, routingKey: String, message: String): String {
        val response = template!!.convertSendAndReceive(exchange.name, routingKey, message) as String?
        println(" [.] Got '$response'")
        return response?: "none"
    }

    fun getName(): String {
        val routingKey = "user.getName"
        return send(nameExchange!!, routingKey, "")
    }

    fun getAccounts(id: String = "getAllAccounts"): String {
        val routingKey = "user.getAccounts"
        return send(accountsExchange!!, routingKey, id)
    }

    fun createAccount(account : String): String {
        val routingKey = "user.createAccount"
        return send(accountCreationExchange!!, routingKey, account)
    }

    fun login(account: String): String {
        val routingKey = "user.login"
        return send(loginExchange!!, routingKey, account)
    }

    fun deleteAccount(id: String): String{
        val routingKey = "user.deleteAccount"
        return send(deleteExchange!!, routingKey, id)
    }

}
