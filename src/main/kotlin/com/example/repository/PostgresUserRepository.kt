package com.example.repository

import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.database.*
import com.example.model.Note
import com.example.model.UserData
import com.sun.jdi.request.DuplicateRequestException
import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import java.time.LocalDateTime

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

    override suspend fun createUserNote(username: String, note: Note): Int = suspendTransaction{
        if (note.content.isNullOrBlank() || note.heading.isNullOrBlank()){
            throw IllegalArgumentException (
                "Heading or content cannot be null"
            )
        }
       val  duplicateName =  UserNotesDao.find {
(UserNotesTable.notesUsername eq  username) and  (UserNotesTable.heading eq  note.heading)
        }.firstOrNull()
        if (duplicateName != null){
            throw DuplicateRequestException ("Cannot duplicate the name of the heading")
        }

        UserNotesDao.new {
            notesUsername  = username
            heading = note.heading
            content  =note.content
            dateCreated = note.dateCreated ?: LocalDateTime.now().toString()
            lastUpdate = note.lastUpdated ?: LocalDateTime.now().toString()
            version = note.version
        }
        val noteId : Int = UserNotesDao.find {( UserNotesTable.heading eq note.heading ) and (UserNotesTable.notesUsername eq username) }.first().id.value
        return@suspendTransaction noteId
    }

    override  suspend fun getUserNote(username: String): List<Note> = suspendTransaction {
      val userdata =   UserNotesDao.find { UserNotesTable.notesUsername eq username}
          .orderBy(UserNotesTable.lastUpdate to SortOrder.DESC)

        if (userdata.count() == 0L){
              throw NotFoundException("No Notes are created by the user")
          }
        else {
        userdata
            .map(::daoToModelForNotes)
        }
    }

    override suspend fun getUserDataWithId(noteId: Int): Note = suspendTransaction {
      val note  =   UserNotesDao.findById(noteId) ?: throw  NullPointerException("no such note is found in the table")
            note.let(::daoToModelForNotes)
    }

    override suspend  fun getUserDataWithDateCreated(username: String): String {
        TODO("Not yet implemented")
    }

    override suspend  fun getUserDataWithLastUpdated(username: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserData(note: Note) : Unit = suspendTransaction {
        if (note.id == null){
            throw IllegalArgumentException("Note ID cannot be null")
        }
     val selectedNote =  UserNotesDao.findById(note.id) ?: throw NotFoundException("Note with ID ${note.id} not found") // Handle the case where the note is not found
        selectedNote.apply {
            if (note.content != null && note.heading != null ){
                version += 1
                 lastUpdate = note.lastUpdated ?: LocalDateTime.now().toString()
                note.content.also { content = it }
                note.heading.also { heading = it }
            }
            else if (note.heading != null){
                version += 1
                lastUpdate.also { lastUpdate = it }
                note.heading.also { heading = it }
            }
            else if(note.content != null) {
                lastUpdate.also { lastUpdate = it }
                version += 1
                note.content.also { content = it }
            } else {
                throw NullPointerException("No changes have made")
            }
              // Assuming you have an 'updatedAt' field
        }
    }
    /*
    here it verifies the username and note id belongs to the same person so and after matching tha credentials
    it allows the function to delete the note
     */
    override suspend fun deleteUserData(noteId: Int, username: String) : Unit  = suspendTransaction {
      val userNote  =   UserNotesDao.find {
         ( UserNotesTable.notesUsername eq username) and (UserNotesTable.id  eq noteId)
      }
        if (userNote.empty()){
            throw IllegalAccessException("Manipulated access requested! , Denied")
        }
        else {
          val note =   UserNotesDao.findById(noteId)
            note?.delete()
        }

    }
}