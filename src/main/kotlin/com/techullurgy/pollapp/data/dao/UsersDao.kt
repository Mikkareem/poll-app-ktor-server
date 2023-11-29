package com.techullurgy.pollapp.data.dao

import com.techullurgy.pollapp.data.tables.UserEntity
import com.techullurgy.pollapp.domain.models.AppUser
import com.techullurgy.pollapp.plugins.dbQuery
import com.techullurgy.pollapp.utils.ErrorCode
import com.techullurgy.pollapp.utils.ServiceResult
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

interface UsersDao {
    suspend fun getAppUserById(id: Long): ServiceResult<AppUser>
    suspend fun getAppUserByName(name: String): ServiceResult<AppUser>
    suspend fun updatePassword(userId: Long, password: String): ServiceResult<Boolean>
    suspend fun create(user: AppUser, password: String): ServiceResult<Long>
    suspend fun delete(id: Long): ServiceResult<Boolean>

    suspend fun authenticate(name: String, password: String): ServiceResult<AppUser>
}

internal class UsersDaoImpl: UsersDao {
    override suspend fun getAppUserById(id: Long): ServiceResult<AppUser> {
        return dbQuery {
            try {
                val user = UserEntity
                    .select { UserEntity.id eq id }
                    .map {
                        AppUser(
                            id = it[UserEntity.id],
                            name = it[UserEntity.name],
                            age = it[UserEntity.age]
                        )
                    }
                    .singleOrNull() ?: return@dbQuery ServiceResult.Failure(ErrorCode.USER_NOT_EXISTS)
                ServiceResult.Success(user)
            } catch (e: ExposedSQLException) {
                ServiceResult.Failure(ErrorCode.DATABASE_ERROR)
            }
        }
    }

    override suspend fun getAppUserByName(name: String): ServiceResult<AppUser> {
        return dbQuery {
            try {
                val user = UserEntity
                    .select { UserEntity.name eq name }
                    .map {
                        AppUser(
                            id = it[UserEntity.id],
                            name = it[UserEntity.name],
                            age = it[UserEntity.age]
                        )
                    }
                    .singleOrNull() ?: return@dbQuery ServiceResult.Failure(ErrorCode.USER_NOT_EXISTS)
                ServiceResult.Success(user)
            } catch (e: ExposedSQLException) {
                ServiceResult.Failure(ErrorCode.DATABASE_ERROR)
            }
        }
    }

    override suspend fun updatePassword(userId: Long, password: String): ServiceResult<Boolean> {
        return dbQuery {
            try {
                val count = UserEntity.update(
                    where = { UserEntity.id eq userId }
                ) {
                    it[this.password] = password
                }
                ServiceResult.Success(count > 0)
            } catch (e: ExposedSQLException) {
                ServiceResult.Failure(ErrorCode.DATABASE_ERROR)
            }
        }
    }

    override suspend fun create(user: AppUser, password: String): ServiceResult<Long> {
        return dbQuery {
            try {
                val id = UserEntity.insert {
                    it[name] = user.name
                    it[age] = user.age
                    it[this.password] = password
                }[UserEntity.id]

                ServiceResult.Success(id)
            } catch (e: ExposedSQLException) {
                ServiceResult.Failure(ErrorCode.DATABASE_ERROR)
            }
        }
    }

    override suspend fun delete(id: Long): ServiceResult<Boolean> {
        return dbQuery {
            try {
                val count = UserEntity.deleteWhere { UserEntity.id eq id }
                ServiceResult.Success(count > 0)
            } catch (e: ExposedSQLException) {
                ServiceResult.Failure(ErrorCode.DATABASE_ERROR)
            }
        }
    }

    override suspend fun authenticate(name: String, password: String): ServiceResult<AppUser> {
        return dbQuery {
            try {
                val appUser = UserEntity.select {
                    (UserEntity.name eq name) and (UserEntity.password eq password)
                }.map {
                    AppUser(
                        id = it[UserEntity.id],
                        name = it[UserEntity.name],
                        age = it[UserEntity.age]
                    )
                }.singleOrNull() ?: return@dbQuery ServiceResult.Failure(ErrorCode.AUTHENTICATION_FAILED)

                ServiceResult.Success(appUser)
            } catch (e: ExposedSQLException) {
                ServiceResult.Failure(ErrorCode.DATABASE_ERROR)
            }
        }
    }
}