package com.techullurgy.pollapp.network.models

import com.techullurgy.pollapp.domain.models.AppPoll
import kotlinx.serialization.Serializable

@Serializable
data class Poll(
    val id: Long,
    val name: String,
    val description: String,
    val questions: List<PollQuestion>
) {
    fun toAppPoll(): AppPoll = AppPoll(
        id = id,
        name = name,
        description = description,
        questions = questions.map { it.toAppPollQuestion() }
    )
}
