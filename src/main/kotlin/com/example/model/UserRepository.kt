package com.example.model

interface UserRepository {
    suspend fun registerUser (userData: UserData)
     suspend  fun verifyUser(userData: UserData) : Boolean
     suspend fun saveUserData(username: String) : Boolean
     suspend fun getUserData (username : String) : String
     suspend  fun getUserDataWithDateCreated(username: String) : String
     suspend fun getUserDataWithLastUpdated(username: String) : String

}

