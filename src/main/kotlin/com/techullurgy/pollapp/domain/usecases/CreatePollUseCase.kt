package com.techullurgy.pollapp.domain.usecases

import com.techullurgy.pollapp.data.dao.PollsDao
import com.techullurgy.pollapp.domain.models.AppPoll
import com.techullurgy.pollapp.utils.ServiceResult

class CreatePollUseCase(
    private val pollsDao: PollsDao
) {
    suspend operator fun invoke(appPoll: AppPoll): ServiceResult<Long> {
        return pollsDao.create(appPoll)
    }
}