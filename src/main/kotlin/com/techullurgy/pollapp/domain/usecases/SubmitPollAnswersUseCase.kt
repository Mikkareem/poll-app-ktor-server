package com.techullurgy.pollapp.domain.usecases

import com.techullurgy.pollapp.data.dao.PollAnswersDao
import com.techullurgy.pollapp.network.models.AnsweredPoll

class SubmitPollAnswersUseCase(
    private val pollAnswersDao: PollAnswersDao
) {
    suspend operator fun invoke(answeredPoll: AnsweredPoll, userId: Long): Boolean {
        return pollAnswersDao.answered(answeredAppPoll = answeredPoll.toAnsweredAppPoll(), userId = userId).data()
    }
}