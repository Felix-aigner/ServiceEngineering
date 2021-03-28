package at.serviceengineering.webservice1.utils

import java.util.*

object Unwrap {

    fun <BannedAccount> Optional<BannedAccount>.unwrapBannedAccount(): BannedAccount? = orElse(null)
}
