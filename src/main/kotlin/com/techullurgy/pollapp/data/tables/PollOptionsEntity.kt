package com.techullurgy.pollapp.data.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object PollOptionsEntity: Table() {
    val id = long("id").autoIncrement()
    val option = varchar("option_name", 50)
    val questionId = long("question_id").references(PollQuestionEntity.id, onDelete = ReferenceOption.CASCADE)
    val optionIndex = integer("option_index")

    init {
        uniqueIndex(option, questionId, optionIndex)
    }

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id, name = "PK_PollOptionsEntity")
}