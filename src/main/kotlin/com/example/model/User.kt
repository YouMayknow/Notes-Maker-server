package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val username : String ,
    val password : String ,
    val email : String ,
    val dateCreated: String = ""
)

