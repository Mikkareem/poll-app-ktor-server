package com.techullurgy.pollapp.data.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object PollQuestionEntity: Table() {
    val id = long("id").autoIncrement()
    val questionTitle = varchar("question_title", 400)
    val pollId = long("poll_id").references(PollEntity.id, onDelete = ReferenceOption.CASCADE)

    init {
        uniqueIndex(questionTitle, pollId)
    }

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id, name = "PK_PollQuestionEntity")
}