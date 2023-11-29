package com.techullurgy.pollapp.network.models

import com.techullurgy.pollapp.domain.models.AppPollQuestion
import kotlinx.serialization.Serializable

@Serializable
data class AssignablePollQuestion(
    val question: String,
    val options: List<AssignablePollOption>
) {
    fun toAppPollQuestion(): AppPollQuestion = AppPollQuestion(
        question = question,
        options = options.map { it.toAppPollOption() }
    )
}