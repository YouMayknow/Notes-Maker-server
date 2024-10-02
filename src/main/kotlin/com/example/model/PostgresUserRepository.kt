package com.example.model

import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.database.UserDataDao
import com.example.database.UserDataTable
import com.example.database.UserDataTable.password
import com.example.database.daoToModel
import com.example.database.suspendTransaction
import io.ktor.server.plugins.*

class PostgresUserRepository : UserRepository {
    override suspend fun registerUser(userData: UserData) : Unit  = suspendTransaction {
      val user =   UserDataDao.find { UserDataTable.username eq userData.username }
            .map { ::daoToModel }
            .firstOrNull()
        if (user != null){
            throw BadRequestException("User already exists")
        }
        val hashPassword = BCrypt.withDefaults().hashToString(12 , userData.password.toCharArray())
        UserDataDao.new{
            username = userData.username
            password = hashPassword
            email = userData.email
        }
    }
    override suspend fun verifyUser(userData: UserData): Boolean = suspendTransaction{
    val user =   UserDataDao.find { UserDataTable.username eq userData.username  }
          .map { daoToModel(it) }
        .firstOrNull()
        if (user == null){
            throw BadRequestException ("No user found with that name")
        }

        val hashedPassword = user.password
        val verify = BCrypt.verifyer().verify(userData.password.toCharArray() , hashedPassword ).verified
        return@suspendTransaction verify
    }

    override suspend fun saveUserData(username: String): Boolean {
        TODO("Not yet implemented")
    }

    override  suspend fun getUserData(username: String): String {
        TODO("Not yet implemented")
    }

    override suspend  fun getUserDataWithDateCreated(username: String): String {
        TODO("Not yet implemented")
    }

    override suspend  fun getUserDataWithLastUpdated(username: String): String {
        TODO("Not yet implemented")
    }
}