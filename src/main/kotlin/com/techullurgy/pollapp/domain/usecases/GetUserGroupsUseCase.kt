package com.techullurgy.pollapp.domain.usecases

import com.techullurgy.pollapp.data.dao.GroupsDao
import com.techullurgy.pollapp.network.models.Group
import com.techullurgy.pollapp.utils.ServiceResult

class GetUserGroupsUseCase(
    private val groupsDao: GroupsDao
) {
    suspend operator fun invoke(userId: Long): ServiceResult<List<Group>> {
        return ServiceResult.Success(groupsDao.getUserGroups(userId = userId).data().map { it.toGroup() })
    }
}