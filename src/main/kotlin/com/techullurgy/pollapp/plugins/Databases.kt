package com.techullurgy.pollapp.plugins

import com.techullurgy.pollapp.data.tables.*
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction


fun Application.configureDatabase() {
    val jdbcUrl = environment.config.property("storage.jdbcUrl").getString()
    val driverClassName = environment.config.property("storage.driverClassName").getString()

    val username = environment.config.propertyOrNull("storage.username")?.getString()
    val password = environment.config.propertyOrNull("storage.password")?.getString()

    Database.connect(
        url = jdbcUrl,
        driver = driverClassName,
        user = username ?: "",
        password = password ?: ""
    )
    transaction {
        SchemaUtils.create(UserEntity, UserSecurity, PollEntity, PollQuestionEntity, PollOptionsEntity, GroupEntity, GroupMembersEntity, PollAssignmentsEntity, PollAssignmentDetailsEntity, PollAnswersEntity, PollAnswerDetailsEntity)
    }
}

suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }

suspend fun <T> tryInTransaction(block: suspend () -> T): T {
    val isTransactionPresent = TransactionManager.currentOrNull() != null

    return if(isTransactionPresent) {
        block()
    } else {
        dbQuery(block)
    }
}