package com.techullurgy.pollapp.domain.models

import com.techullurgy.pollapp.network.models.AnsweredPoll

data class AnsweredAppPoll(
    val appPoll: AppPoll,
    val answers: List<Long>
) {
    fun toAnsweredPoll(): AnsweredPoll = AnsweredPoll(
        poll = appPoll.toPoll(),
        answers = answers
    )
}
