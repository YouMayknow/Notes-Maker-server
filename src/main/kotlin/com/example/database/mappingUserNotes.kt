package com.example.database

import com.example.model.Note
import com.example.plugins.UserService.Users.id
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object UserNotesTable : IntIdTable(name = "user_notes_table") {
    var notesUsername = varchar("username",50).index()
    var heading = varchar("heading" , 200)
    var content = text("content")
    var dateCreated = datetime("date_created").defaultExpression(CurrentDateTime)
    var lastUpdate = datetime("last_updated").defaultExpression(CurrentDateTime)
}


class UserNotesDao (id : EntityID<Int>) : IntEntity(id = id){
    companion object : IntEntityClass<UserNotesDao>(UserNotesTable)

    var notesUsername by UserNotesTable.notesUsername
    var heading by UserNotesTable.heading
    var content by UserNotesTable.content
    var dateCreated by UserNotesTable.dateCreated
    var lastUpdate by UserNotesTable.lastUpdate
}


fun daoToModelForNotes(dao : UserNotesDao) = Note(
    id = dao.id.value,
    username = dao.notesUsername,
    heading = dao.heading,
    lastCreated = dao.lastUpdate.toString(),
    content =  dao.content,
    dateCreated =  dao.dateCreated.toString()
)

