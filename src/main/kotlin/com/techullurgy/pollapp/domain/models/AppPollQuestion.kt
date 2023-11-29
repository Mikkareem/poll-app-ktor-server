package com.techullurgy.pollapp.domain.models

import com.techullurgy.pollapp.network.models.PollQuestion

data class AppPollQuestion(
    val id: Long = -1,
    val question: String,
    val options: List<AppPollOption>
) {
    fun toPollQuestion(): PollQuestion = PollQuestion(
        id = id,
        question = question,
        options = options.map { it.toPollOption() }
    )
}
