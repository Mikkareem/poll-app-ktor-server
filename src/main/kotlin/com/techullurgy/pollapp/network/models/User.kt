package com.techullurgy.pollapp.network.models

import com.techullurgy.pollapp.domain.models.AppUser
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long = -1,
    val name: String,
    val age: Int
) {
    fun toAppUser(): AppUser = AppUser(id = id, name = name, age = age)
}