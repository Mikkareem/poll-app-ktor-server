package com.techullurgy.pollapp.domain.usecases

import com.techullurgy.pollapp.data.dao.GroupsDao
import com.techullurgy.pollapp.utils.ServiceResult

class AddMemberToGroupUseCase(
    private val groupsDao: GroupsDao
) {
    suspend operator fun invoke(groupId: Long, userId: Long): ServiceResult<Boolean> {
        return groupsDao.addUserToGroup(groupId = groupId, userId = userId)
    }
}