package at.serviceengineering.microservice.user.repositories

import at.serviceengineering.microservice.user.entities.Account
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository: MongoRepository<Account, String> {
    fun findByUsername(username: String): Account?
}
