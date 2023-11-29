package com.techullurgy.pollapp.data.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object PollAnswerDetailsEntity: Table() {
    val id = long("id").autoIncrement()
    val pollAnswerId = long("poll_answer_id").references(PollAnswersEntity.id, onDelete = ReferenceOption.CASCADE)
    val questionId = long("poll_question_id").references(PollQuestionEntity.id, onDelete = ReferenceOption.CASCADE)
    val selectedOption = long("selected_poll_option_id").references(PollOptionsEntity.id, onDelete = ReferenceOption.CASCADE)

    init {
        uniqueIndex(pollAnswerId, questionId, selectedOption)
    }

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id, name="PK_PollAnswerDetailsEntity")
}