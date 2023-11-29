package com.techullurgy.pollapp.data.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object PollAnswersEntity: Table() {
    val id = long("id").autoIncrement()
    val pollId = long("poll_id").references(PollEntity.id, onDelete = ReferenceOption.CASCADE)
    val attenderId = long("attender_id").references(UserEntity.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id, name = "PK_PollAnswersEntity")
}