package com.techullurgy.pollapp.domain.usecases

import com.techullurgy.pollapp.data.dao.PollAnswersDao
import com.techullurgy.pollapp.network.models.AnsweredPoll
import com.techullurgy.pollapp.utils.ServiceResult

class SubmitPollUseCase(
    private val pollAnswersDao: PollAnswersDao
) {
    suspend operator fun invoke(answeredPoll: AnsweredPoll, userId: Long): ServiceResult<Boolean> {
        return pollAnswersDao.answered(
            answeredAppPoll = answeredPoll.toAnsweredAppPoll(),
            userId = userId
        )
    }
}