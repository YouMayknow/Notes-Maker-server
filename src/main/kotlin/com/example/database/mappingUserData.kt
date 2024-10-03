package com.example.database

import com.example.model.UserData
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object UserDataTable : IntIdTable(name = "userData") {
    var username =  varchar("username" , 50).index()
    var password = varchar("password", 200)
    var email = varchar("email", 50)
    var dateCreated = datetime("date_created").defaultExpression(CurrentDateTime)
}
class UserDataDao(id:EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDataDao>(UserDataTable)
    var username by UserDataTable.username
    var password by UserDataTable.password
    var email by UserDataTable.email
    var dateCreated by UserDataTable.dateCreated
}

suspend fun  <T> suspendTransaction(block : Transaction.() -> T) : T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)
fun daoToModel(dao: UserDataDao) = UserData(
    username = dao.username,
    password = dao.password,
    email = dao.email,
  dateCreated = dao.dateCreated.toString()
)