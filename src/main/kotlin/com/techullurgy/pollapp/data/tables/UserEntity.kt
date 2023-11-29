package com.techullurgy.pollapp.data.tables

import org.jetbrains.exposed.sql.Table

object UserEntity: Table() {
    val id = long("id").autoIncrement()
    val name = varchar("name", 30)
    val password = varchar("password", 30)
    val age = integer("age")

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id, name = "PK_UserEntity")
}