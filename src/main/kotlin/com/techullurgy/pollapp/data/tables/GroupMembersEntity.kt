package com.techullurgy.pollapp.data.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object GroupMembersEntity: Table() {
    val id = long("id").autoIncrement()
    val user = long("user_id").references(UserEntity.id, onDelete = ReferenceOption.CASCADE)
    val group = long("group_id").references(GroupEntity.id, onDelete = ReferenceOption.CASCADE)

    init {
        uniqueIndex(user, group)
    }

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id, name = "PK_GroupMembersEntity")
}