package com.techullurgy.pollapp.domain.usecases

import com.techullurgy.pollapp.data.dao.UsersDao
import com.techullurgy.pollapp.domain.models.AppUser
import com.techullurgy.pollapp.network.models.User
import com.techullurgy.pollapp.utils.ServiceResult

class CreateUserUseCase(
    private val usersDao: UsersDao
) {
    suspend operator fun invoke(name: String, password: String, age: Int): ServiceResult<User> {
        val newUserId = usersDao.create(AppUser(name = name, age = age), password).data()
        val newUser = usersDao.getAppUserById(newUserId).data().toUser()
        return ServiceResult.Success(newUser)
    }
}