package com.techullurgy.pollapp.network.requests

import com.techullurgy.pollapp.network.models.AnsweredPoll
import kotlinx.serialization.Serializable

@Serializable
data class SubmitPollAnswersRequest(
    val answeredPoll: AnsweredPoll
)