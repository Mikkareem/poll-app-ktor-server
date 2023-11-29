package com.techullurgy.pollapp.domain.models

import com.techullurgy.pollapp.network.models.Group

data class AppGroup(
    val id: Long = -1,
    val name: String,
    val owner: AppUser,
    val members: List<AppUser>
) {
    fun toGroup(): Group = Group(
        id = id,
        name = name,
        owner = owner.toUser(),
        members = members.map { it.toUser() }
    )
}
