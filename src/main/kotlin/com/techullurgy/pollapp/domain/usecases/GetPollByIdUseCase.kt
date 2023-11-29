package com.techullurgy.pollapp.domain.usecases

import com.techullurgy.pollapp.data.dao.PollsDao
import com.techullurgy.pollapp.domain.models.AppPoll
import com.techullurgy.pollapp.utils.ServiceResult

class GetPollByIdUseCase(
    private val pollsDao: PollsDao
) {
    suspend operator fun invoke(pollId: Long): ServiceResult<AppPoll> {
        return pollsDao.getPollDetailsById(pollId)
    }
}