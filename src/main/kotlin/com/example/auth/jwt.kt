package com.example.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import java.util.Date

object JwtConfig {
    private const val secretKey = "secretKey"
    private const val validInHours = 36_00_000*60
    private const val issuer = "myapp"
    private val algorithm = Algorithm.HMAC256(secretKey)


    fun generateToken (username : String): String =
        JWT.create()
            .withIssuer(issuer)
            .withClaim("username" ,username)
            .withExpiresAt(Date(System.currentTimeMillis() + validInHours))
            .sign(algorithm)

    fun configureVerifier() : JWTVerifier = JWT.require(algorithm)
        .withIssuer(issuer)
        .build()
}
fun configureJWT(environment: ApplicationEnvironment) : JWTVerifier {
    return JwtConfig.configureVerifier()
}

