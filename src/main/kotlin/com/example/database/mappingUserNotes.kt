package com.example.database

import com.example.model.Note
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object UserNotesTable : IntIdTable(name = "user_notes_table") {
    val notesUsername = varchar("username",50)
    val heading = varchar("heading" , 200)
    val content = text("content")
    val dateCreated = datetime("date_created").defaultExpression(CurrentDateTime)
    val lastUpdate = datetime("last_updated").defaultExpression(CurrentDateTime)
}

class UserNotesDao (id : EntityID<Int>) : IntEntity(id = id){
    companion object : IntEntityClass<UserDataDao>(UserNotesTable)
    var notesUsername by UserNotesTable.notesUsername
    var heading by UserNotesTable.heading
    var content by UserNotesTable.content
    var dateCreated by UserNotesTable.dateCreated
    var lastUpdate by UserNotesTable.lastUpdate
}

fun daoToModelForNotes(dao : UserNotesDao) = Note(
    username = dao.notesUsername,
    heading = dao.heading ,
    content =  dao.content ,
    dateCreated =  dao.dateCreated.toString() ,
    lastCrated = dao.lastUpdate.toString()

)

