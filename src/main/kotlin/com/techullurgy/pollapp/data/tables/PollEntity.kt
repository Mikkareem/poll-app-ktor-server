package com.techullurgy.pollapp.data.tables

import org.jetbrains.exposed.sql.Table

object PollEntity: Table() {
    val id = long("poll_id").autoIncrement()
    val title = varchar("poll_title", 50)
    val description = varchar("poll_description", 100)

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id, name = "PK_PollEntity")
}