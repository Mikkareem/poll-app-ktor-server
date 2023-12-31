package com.techullurgy.pollapp.network.models

import com.techullurgy.pollapp.domain.models.AnsweredAppPoll
import com.techullurgy.pollapp.network.responses.AnsweredPollResponse
import kotlinx.serialization.Serializable

@Serializable
data class AnsweredPoll(
    val poll: Poll,
    val answers: List<Long>
) {
    fun toAnsweredAppPoll(): AnsweredAppPoll = AnsweredAppPoll(
        appPoll = poll.toAppPoll(),
        answers = answers
    )

    fun toAnsweredPollResponse(): AnsweredPollResponse = AnsweredPollResponse(
        poll = poll,
        answers = answers
    )
}
