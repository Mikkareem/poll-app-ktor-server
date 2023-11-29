package com.techullurgy.pollapp.data.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object GroupEntity: Table() {
    val id = long("id").autoIncrement()
    val name = varchar("group_name", 25)
    val owner = long("owner_user_id").references(UserEntity.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id, name = "PK_GroupEntity")
}