package com.example.routes

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.protectedRoutes() {
    authenticate("auth-jwt") {
        get("/notes/new") {
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("username").asString()
            call.respond(HttpStatusCode.OK, "hey you got the inner information beautifully : $username", )
        }
    }
}