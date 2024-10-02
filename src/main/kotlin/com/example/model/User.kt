package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val username : String ,
    val password : String ,
    val email : String ,
    val dateCreated: String = ""
)

@Serializable
data class Note (
    val username : String,
    val heading : String,
    val lastCrated : String ,
    val content : String ,
    val dateCreated : String ,
)
