package com.techullurgy.pollapp.domain.usecases

import com.techullurgy.pollapp.data.dao.GroupsDao
import com.techullurgy.pollapp.network.models.User
import com.techullurgy.pollapp.utils.ServiceResult

class GetGroupMembersUseCase(
    private val groupsDao: GroupsDao
) {
    suspend operator fun invoke(groupId: Long): ServiceResult<List<User>> {
        return ServiceResult.Success(groupsDao.getAllAssignableUsers(groupId = groupId).data().map { it.toUser() })
    }
}