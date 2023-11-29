package com.techullurgy.pollapp.network.models

import com.techullurgy.pollapp.domain.models.AppPollQuestion
import kotlinx.serialization.Serializable

@Serializable
data class PollQuestion(
    val id: Long,
    val question: String,
    val options: List<PollOption>
) {
    fun toAppPollQuestion(): AppPollQuestion = AppPollQuestion(
        id = id,
        question = question,
        options = options.map { it.toAppPollOption() }
    )
}
