package com.techullurgy.pollapp.domain.usecases

import com.techullurgy.pollapp.data.dao.PollAnswersDao
import com.techullurgy.pollapp.data.dao.PollsDao
import com.techullurgy.pollapp.network.responses.PollResponse

class GetAssignedPollsForUserUseCase(
    private val pollsDao: PollsDao,
    private val pollAnswersDao: PollAnswersDao
) {
    suspend operator fun invoke(userId: Long): List<PollResponse> {
        val appPolls = pollsDao.getAllAssignedPollsForUser(userId = userId).data()
        val answeredAppPolls = appPolls.filter {
            pollAnswersDao.isUserAnsweredThePoll(userId = userId, pollId = it.id).data()
        }

        val answeredPollResponses = answeredAppPolls.map {
            pollAnswersDao.getAnsweredPollForUser(userId = userId, pollId = it.id).data().toAnsweredPoll().toAnsweredPollResponse()
        }

        val nonAnsweredPollResponses = appPolls
            .filter { !answeredAppPolls.contains(it) }
            .map { it.toPoll().toNonAnsweredPollResponse() }

        return answeredPollResponses + nonAnsweredPollResponses
    }
}