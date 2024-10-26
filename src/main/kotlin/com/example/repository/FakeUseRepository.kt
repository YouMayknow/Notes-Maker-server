package com.example.repository

import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.model.Note
import com.example.model.NoteCreate
import com.example.model.NoteUpdate
import com.example.model.UserData
import io.ktor.server.plugins.*
import java.util.concurrent.ConcurrentHashMap

class fakeUserRepository : UserRepository {
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
    override suspend fun saveUserData(username: String, note: NoteCreate): Int {
        TODO("Not yet implemented")
    }
    override suspend fun getUserData(username: String): List<Note> {
        TODO("Not yet implemented")
    }
    override suspend fun getUserDataWithId(noteId: Int): Note {
        TODO("Not yet implemented")
    }
    override suspend fun getUserDataWithDateCreated(username: String): String {
        TODO("Not yet implemented")
    }
    override suspend fun getUserDataWithLastUpdated(username: String): String {
        TODO("Not yet implemented")
    }
    override suspend fun updateUserData(note: NoteUpdate) {
        TODO("Not yet implemented")
    }
    override suspend fun deleteUserData(noteId: Int, username: String) {
        TODO("Not yet implemented")
    }
}