package com.techullurgy.pollapp.domain.models

import com.techullurgy.pollapp.network.models.User

data class AppUser(
    val id: Long = -1,
    val name: String,
    val age: Int
) {
    fun toUser(): User = User(id = id, name = name, age = age)
}
