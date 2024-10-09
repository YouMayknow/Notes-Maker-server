package com.example.repository

import com.example.model.Note
import com.example.model.NoteCreate
import com.example.model.NoteUpdate
import com.example.model.UserData

interface UserRepository {
    suspend fun registerUser (userData: UserData)
     suspend  fun verifyUser(userData: UserData) : Boolean
     suspend fun saveUserData(username: String, note: NoteCreate)
 suspend fun getUserData (username : String) : List<Note>
 suspend fun getUserDataWithId(noteId: Int) : Note
    suspend  fun getUserDataWithDateCreated(username: String) : String
     suspend fun getUserDataWithLastUpdated(username: String) : String
     suspend fun  updateUserData(note: NoteUpdate)
     suspend fun deleteUserData(noteId : Int , username: String )
}

