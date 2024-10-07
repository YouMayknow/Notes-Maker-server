package com.example.plugins

import com.example.auth.authRoutes
import com.example.repository.PostgresUserRepository
import com.example.routes.protectedRoutes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
) {
//    install(StatusPages) {
//        exception<Throwable> { call, cause ->
//            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
//        }
//    }
    routing {
        get("/he") {
            call.respond(HttpStatusCode.OK , "everything is clear")
        }
        authRoutes(PostgresUserRepository())
        protectedRoutes(userRepository = PostgresUserRepository())
    }

}
