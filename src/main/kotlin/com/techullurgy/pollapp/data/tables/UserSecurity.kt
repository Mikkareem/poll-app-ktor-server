package com.techullurgy.pollapp.data.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object UserSecurity: Table() {
    val id = long("id").autoIncrement()
    val user = long("user_id").references(UserEntity.id, onDelete = ReferenceOption.CASCADE).uniqueIndex()
    val token = varchar("token", 500)

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id, name = "PK_UserSecurity")
}