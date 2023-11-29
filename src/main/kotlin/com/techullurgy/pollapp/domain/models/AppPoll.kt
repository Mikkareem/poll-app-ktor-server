package com.techullurgy.pollapp.domain.models

import com.techullurgy.pollapp.network.models.Poll

data class AppPoll(
    val id: Long = -1,
    val name: String,
    val description: String,
    val questions: List<AppPollQuestion>
) {
    fun toPoll(): Poll = Poll(
        id = id,
        name = name,
        description = description,
        questions = questions.map { it.toPollQuestion() }
    )
}