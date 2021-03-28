package at.serviceengineering.webservice1.utils

import org.apache.commons.codec.digest.DigestUtils.sha256Hex

object HashUtil {

    fun hash(input: String): String
            = sha256Hex(input)
}
