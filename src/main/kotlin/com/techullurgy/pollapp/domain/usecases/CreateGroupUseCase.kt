package com.techullurgy.pollapp.domain.usecases

import com.techullurgy.pollapp.data.dao.GroupsDao
import com.techullurgy.pollapp.utils.ServiceResult

class CreateGroupUseCase(
    private val groupsDao: GroupsDao
) {
    suspend operator fun invoke(groupName: String, ownerId: Long): ServiceResult<Long> {
        return groupsDao.create(name = groupName, userId = ownerId)
    }
}