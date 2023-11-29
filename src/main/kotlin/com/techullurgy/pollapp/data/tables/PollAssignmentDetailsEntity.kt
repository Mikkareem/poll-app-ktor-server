package com.techullurgy.pollapp.data.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object PollAssignmentDetailsEntity: Table() {
    val id = long("id").autoIncrement()
    val userId = long("assigned_user").references(UserEntity.id, onDelete = ReferenceOption.CASCADE)
    val pollAssignmentId = long("poll_assignment_id").references(PollAssignmentsEntity.id, onDelete = ReferenceOption.CASCADE)
    val isCompleted = bool("is_completed").default(false)

    init {
        uniqueIndex(userId, pollAssignmentId)
    }

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id, name = "PK_PollAssignmentDetailsEntity")
}