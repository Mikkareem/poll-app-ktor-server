package com.techullurgy.pollapp.di

import com.techullurgy.pollapp.auth.JwtConfiguration
import com.techullurgy.pollapp.data.dao.*
import com.techullurgy.pollapp.domain.usecases.*
import org.koin.dsl.module

val appModule = module {
    single { JwtConfiguration() }

    single<GroupsDao> { GroupsDaoImpl() }
    single<UsersDao> { UsersDaoImpl() }
    single<PollsDao> { PollsDaoImpl(get()) }
    single<SecurityDao> { SecurityDaoImpl() }
    single<PollAnswersDao> { PollAnswersDaoImpl(get()) }

    single { CreateUserUseCase(get()) }
    single { LoginUserUseCase(get()) }
    single { CreatePollUseCase(get()) }
    single { AssignPollUseCase(get()) }
    single { GetPollByIdUseCase(get()) }
    single { GetAssignedPollsForUserUseCase(get(), get()) }
    single { CreateGroupUseCase(get()) }
    single { AddMemberToGroupUseCase(get()) }
    single { GetGroupMembersUseCase(get()) }
    single { SubmitPollAnswersUseCase(get()) }
    single { GetUserGroupsUseCase(get()) }
}