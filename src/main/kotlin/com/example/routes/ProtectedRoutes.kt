package com.example.routes

import com.example.model.NoteCreate
import com.example.model.NoteUpdate
import com.example.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.protectedRoutes(
    userRepository: UserRepository
) {
    authenticate("auth-jwt") {
        post("/notes/new") {
            val notes = call.receive<NoteCreate>()
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("username").asString()
            try {
                userRepository.saveUserData(username,notes)
            } catch (e : Exception){
                call.respond(HttpStatusCode.BadRequest ,e.message.toString())
            }
            call.respond(HttpStatusCode.OK , "i $username, had gotten the data that i was demanding with header = ${notes.heading} ")
        }

        patch("/notes/update") {
            val note = call.receive<NoteUpdate>()
            try {
                userRepository.updateUserData(note)
            } catch (e: NullPointerException) {
                call.respond(HttpStatusCode.BadRequest, "${e.message}")
            }
            call.respond(HttpStatusCode.OK, "Data of the user is updated ")
        }
        /*this route will throw th exception on not founding
        the data
         */
        get("/notes/all") {
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("username").asString()
            try {
               val notes =  userRepository.getUserData(username)
                call.respond(notes)
            } catch (e : NotFoundException){
                call.respond(HttpStatusCode.NotFound , "${e.message}")
            }
        }
        get("/notes/{id}") {
            val noteId = call.parameters["id"]
            if (noteId.isNullOrBlank()){
                call.respond(HttpStatusCode.BadRequest, "no such note with this id found")
                return@get
            }
            try {
              val notes =   userRepository.getUserDataWithId(noteId.toInt())
                call.respond(HttpStatusCode.OK,notes)
            }
            catch (e : NullPointerException){
                call.respond(HttpStatusCode.NotFound, e.message.toString())
            }
        }
        get("/token/verifier"){
            call.respond(HttpStatusCode.OK , "Token is verifier as it pass the verification")
        }
        delete("/notes/delete/{id}") {
            val noteId = call.parameters["id"]
            if (noteId.isNullOrBlank()){
                call.respond(HttpStatusCode.BadRequest ,"No noteID found in the request")
            } else {
               try {
                   val response = call.principal<JWTPrincipal>()
                   val userName = response!!.payload.getClaim("username").asString()
                   userRepository.deleteUserData(noteId.toInt() , userName)
                   call.respond(HttpStatusCode.OK , "Note Deleted Successful")
               } catch (e : IllegalAccessException) {
                   call.respond(HttpStatusCode.Unauthorized , "${e.message}")
               }
            }
        }
    }
}