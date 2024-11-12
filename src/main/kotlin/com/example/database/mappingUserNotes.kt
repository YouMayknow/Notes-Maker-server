package com.example.database

import com.example.model.Note
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object UserNotesTable : IntIdTable(name = "user_notes_table") {
    var notesUsername = varchar("username",50).index()
    var heading = varchar("heading" , 200)
    var content = text("content")
    var version = integer("version").default(1)
    var dateCreated = varchar("date_created" , 50)
    var lastUpdate = varchar("last_updated" , 50)
}
class UserNotesDao (id : EntityID<Int>) : IntEntity(id = id){
    companion object : IntEntityClass<UserNotesDao>(UserNotesTable)

    var notesUsername by UserNotesTable.notesUsername
    var heading by UserNotesTable.heading
    var content by UserNotesTable.content
    var dateCreated by UserNotesTable.dateCreated
    var lastUpdate by UserNotesTable.lastUpdate
    var version by UserNotesTable.version
}


fun daoToModelForNotes(dao : UserNotesDao) = Note(
    id = dao.id.value,
    username = dao.notesUsername,
    heading = dao.heading,
    lastUpdated = dao.lastUpdate.toString(),
    content =  dao.content,
    dateCreated =  dao.dateCreated.toString(),
    version = dao.version
)

