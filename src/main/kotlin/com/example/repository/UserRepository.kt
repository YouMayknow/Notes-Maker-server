package com.example.repository

import com.example.model.Note
import com.example.model.UserData

interface UserRepository {
    suspend fun registerUser (userData: UserData)
     suspend  fun verifyUser(userData: UserData) : Boolean
     suspend fun createUserNote(username: String, note: Note) : Int
    suspend fun getUserNote (username : String) : List<Note>
 suspend fun getUserDataWithId(noteId: Int) : Note
    suspend  fun getUserDataWithDateCreated(username: String) : String
     suspend fun getUserDataWithLastUpdated(username: String) : String
     suspend fun  updateUserData(note: Note)
     suspend fun deleteUserData(noteId : Int , username: String )
}

