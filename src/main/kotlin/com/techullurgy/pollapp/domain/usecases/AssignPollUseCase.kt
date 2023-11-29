package com.techullurgy.pollapp.domain.usecases

import com.techullurgy.pollapp.data.dao.PollsDao
import com.techullurgy.pollapp.network.models.PollAssignType
import com.techullurgy.pollapp.utils.ServiceResult

class AssignPollUseCase(
    private val pollsDao: PollsDao
) {
    suspend operator fun invoke(pollId: Long, pollAssignType: PollAssignType, assignId: Long): ServiceResult<Boolean> {
        return pollsDao.assign(pollId = pollId, assignType = pollAssignType, assignId = assignId)
    }
}