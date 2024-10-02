package com.example.model

import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.database.UserDataTable.username
import io.ktor.server.plugins.*
import java.util.concurrent.ConcurrentHashMap

class fakeUseRepository : UserRepository {
    private val users = ConcurrentHashMap<String , String>()


    override suspend fun registerUser(userData: UserData) {
        if (users.containsKey(userData.username)){
            throw BadRequestException("User already exists")
        }
        val hashPassword = BCrypt.withDefaults().hashToString(12 , userData.password.toCharArray())
        users[userData.username] = hashPassword
    }

    override suspend fun verifyUser(userData: UserData): Boolean {
        if (!users.containsKey(userData.username)){
            throw BadRequestException("No user found with that name")
        }
        val hashedPassword = users[userData.username]
        val verify = BCrypt.verifyer().verify(userData.password.toCharArray() , hashedPassword ).verified
        return verify
    }

    override suspend fun saveUserData(username: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getUserData(username: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun getUserDataWithDateCreated(username: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun getUserDataWithLastUpdated(username: String): String {
        TODO("Not yet implemented")
    }
}