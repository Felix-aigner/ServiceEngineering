package at.serviceengineering.webservice1.repositories

import at.serviceengineering.webservice1.entities.Account
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IAccountRepository : JpaRepository<Account, UUID> {

    fun findAccountByUsername(username: String): Account?

    fun deleteAccountByUsername(username: String)
}
