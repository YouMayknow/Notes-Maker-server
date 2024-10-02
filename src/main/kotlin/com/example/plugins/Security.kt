package com.example.plugins

import com.example.auth.configureJWT
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import java.nio.file.Paths.get

fun Application.configureSecurity() {
    install(Authentication){
       jwt("auth-jwt") {
           realm = "myRealm"
           verifier( configureJWT(environment = this@configureSecurity.environment))
           validate { jwtCredential ->
               if (jwtCredential.payload.getClaim("username").asString() != null){
                   JWTPrincipal(jwtCredential.payload)
               } else null
           }
       }
    }
}
