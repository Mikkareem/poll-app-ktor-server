package com.techullurgy.pollapp.domain.usecases

import com.techullurgy.pollapp.data.dao.PollsDao
import com.techullurgy.pollapp.domain.models.AppPoll
import com.techullurgy.pollapp.utils.ServiceResult

class GetAssignedPollsForUserUseCase(
    private val pollsDao: PollsDao
) {
    suspend operator fun invoke(userId: Long): ServiceResult<List<AppPoll>> {
        return pollsDao.getAllAssignedPollsForUser(userId = userId)
    }
}