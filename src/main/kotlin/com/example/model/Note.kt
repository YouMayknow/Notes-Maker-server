package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id : Int? = null,
    val username : String? = null,
    val heading : String?,
    val lastUpdated : String? = null,
    val content : String?,
    val dateCreated : String? = null,
    val version : Int = 1
)

@Serializable
data class  NoteUpdate(
    val content: String? = null,
    val heading : String? = null ,
    val id : Int
)

@Serializable
data class  NoteCreate(
    val heading: String ,
    val content : String ,

)
