package com.techullurgy.pollapp.network.models

import com.techullurgy.pollapp.domain.models.AppPoll
import kotlinx.serialization.Serializable

@Serializable
data class AssignablePoll(
    val name: String,
    val description: String,
    val questions: List<AssignablePollQuestion>
) {
    fun toAppPoll(): AppPoll = AppPoll(
        name = name,
        description = description,
        questions = questions.map { it.toAppPollQuestion() }
    )
}