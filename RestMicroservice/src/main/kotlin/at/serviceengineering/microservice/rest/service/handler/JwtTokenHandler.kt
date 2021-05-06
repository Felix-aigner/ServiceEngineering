package at.serviceengineering.microservice.rest.service.handler

import at.serviceengineering.microservice.rest.service.exceptions.TokenNotValidException
import io.fusionauth.jwt.Signer
import io.fusionauth.jwt.Verifier
import io.fusionauth.jwt.domain.JWT
import io.fusionauth.jwt.hmac.HMACSigner
import io.fusionauth.jwt.hmac.HMACVerifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.ZoneOffset
import java.time.ZonedDateTime

@Service
class JwtTokenHandler(
) {

    @Value("\${token.privateKey}")
    val privateKey: String? = null
    @Value("\${token.issuer}")
    val issuer: String? = null
    @Value("\${token.expirationTime}")
    val expirationTime: Long = 0

    fun buildJwt(id: String): String {
        val signer: Signer = HMACSigner.newSHA256Signer(privateKey)
        val jwt = JWT().setIssuer(issuer)
                .setIssuedAt(ZonedDateTime.now(ZoneOffset.UTC))
                .setSubject(id)
                .setExpiration(ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(expirationTime))
        return JWT.getEncoder().encode(jwt, signer)
    }

    fun recoverJWT(encodedJWT: String): JWT {
        return try {
            val verifier: Verifier = HMACVerifier.newVerifier(privateKey)
            JWT.getDecoder().decode(encodedJWT, verifier)
        } catch (e: Exception) {
            throw TokenNotValidException()
        }
    }

}
