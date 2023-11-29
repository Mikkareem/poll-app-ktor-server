package com.techullurgy.pollapp.data.tables

import com.techullurgy.pollapp.network.models.PollAssignType
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object PollAssignmentsEntity: Table() {
    val id = long("assignment_id").autoIncrement()
    val assignmentType = enumeration<PollAssignType>("assignment_type")
    val assignedId = long("assigned_id")
    val poll = long("poll_id").references(PollEntity.id, onDelete = ReferenceOption.CASCADE)

    init {
        uniqueIndex(poll, assignedId, assignmentType)
    }

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id, name = "PK_PollAssignmentsEntity")
}