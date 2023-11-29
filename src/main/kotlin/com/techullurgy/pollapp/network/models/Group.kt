package com.techullurgy.pollapp.network.models

import com.techullurgy.pollapp.domain.models.AppGroup
import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val id: Long = -1,
    val name: String,
    val owner: User,
    val members: List<User>
) {
    fun toAppGroup(): AppGroup = AppGroup(
        id = id,
        name = name,
        owner = owner.toAppUser(),
        members = members.map { it.toAppUser() }
    )
}
