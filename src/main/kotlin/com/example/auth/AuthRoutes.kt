package com.example.auth

import com.example.model.UserData
import com.example.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes (
     userRepository: UserRepository
) {
    post("/register") {
        val params = call.receiveParameters()
        val username = params["username"]
        val password = params["password"]
        if (username.isNullOrBlank() || password.isNullOrBlank()) {
            call.respond(HttpStatusCode.BadRequest, "Username and password cannot be null" ,)
            return@post
        }  else if (password.length <= 8 ){
            call.respond(HttpStatusCode.BadRequest , "Password is too short at-least 8 digits")
            return@post
        } else {
            try {
                userRepository.registerUser(UserData(username, password, "fasdf", "Fad"))
                call.respond(HttpStatusCode.OK , "User $username ,  Registered successfully")
            } catch (e : BadRequestException){
                call.respond(HttpStatusCode.BadRequest ,"${e.message}" )
            }
        }
    }
    post("/login") {
        val params = call.receiveParameters()
        val username = params["username"]
        val password = params["password"]
        if (username.isNullOrBlank() || password.isNullOrBlank()) {
            call.respond(HttpStatusCode.BadRequest, "Username and password cannot be null" ,)
            return@post
        } else {
            try {
          val verifyUser =   userRepository.verifyUser(UserData(username = username, password, "asdfd", "fd"))
                if (verifyUser){
                   val token =  JwtConfig.generateToken(username)
                    call.respond( HttpStatusCode.OK , mapOf("token" to token))
                }
                else {
                    call.respond(HttpStatusCode.Unauthorized , "Password is wrong")
                }
            } catch (e : BadRequestException){
                call.respond(HttpStatusCode.BadRequest , "${e.message}" )
            }
        }
    }
}