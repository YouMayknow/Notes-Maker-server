package com.example.plugins

import com.example.auth.configureJWT
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

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
           challenge{_,_ ->
               call.respond(HttpStatusCode.Unauthorized , "User is not verified")
           }
       }
    }
}
