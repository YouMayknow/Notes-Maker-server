package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id : Int  ,
    val username : String?,
    val heading : String?,
    val lastCreated : String? = "" ,
    val content : String? ,
    val dateCreated : String? = ""
)

@Serializable
data class  NoteUpdate(
    val content: String? = null,
    val heading : String? = null
)

@Serializable
data class  NoteCreate(
    val heading: String ,
    val content : String ,
)
