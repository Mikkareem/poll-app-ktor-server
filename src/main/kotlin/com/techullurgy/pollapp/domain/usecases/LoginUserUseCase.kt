package com.techullurgy.pollapp.domain.usecases

import com.techullurgy.pollapp.data.dao.UsersDao
import com.techullurgy.pollapp.network.models.User
import com.techullurgy.pollapp.utils.ServiceResult

class LoginUserUseCase(
    private val usersDao: UsersDao
) {
    suspend operator fun invoke(username: String, password: String): ServiceResult<User> {
        val user = usersDao.authenticate(name = username, password = password).data().toUser()
        return ServiceResult.Success(user)
    }
}