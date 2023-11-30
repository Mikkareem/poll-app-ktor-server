package com.techullurgy.pollapp.data.dao

import com.techullurgy.pollapp.data.tables.GroupEntity
import com.techullurgy.pollapp.data.tables.GroupMembersEntity
import com.techullurgy.pollapp.data.tables.UserEntity
import com.techullurgy.pollapp.domain.models.AppGroup
import com.techullurgy.pollapp.domain.models.AppUser
import com.techullurgy.pollapp.plugins.dbQuery
import com.techullurgy.pollapp.plugins.tryInTransaction
import com.techullurgy.pollapp.utils.ErrorCode
import com.techullurgy.pollapp.utils.ServiceResult
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

interface GroupsDao {
    suspend fun create(name: String, userId: Long): ServiceResult<Long>
    suspend fun delete(id: Long): ServiceResult<Boolean>
    suspend fun getGroupById(id: Long): ServiceResult<AppGroup>
    suspend fun addUserToGroup(groupId: Long, userId: Long): ServiceResult<Boolean>
    suspend fun getAllAssignableUsers(groupId: Long): ServiceResult<List<AppUser>>
}

internal class GroupsDaoImpl: GroupsDao {
    override suspend fun create(name: String, userId: Long): ServiceResult<Long> {
        return try {
            val id = dbQuery {
                GroupEntity.insert {
                    it[this.name] = name
                    it[owner] = userId
                }[GroupEntity.id]
            }

            ServiceResult.Success(id)
        } catch (e: ExposedSQLException) {
            ServiceResult.Failure(ErrorCode.DATABASE_ERROR)
        }
    }

    override suspend fun delete(id: Long): ServiceResult<Boolean> {
        return try {
            val count = dbQuery { GroupEntity.deleteWhere { GroupEntity.id eq id } }
            ServiceResult.Success(count > 0)
        } catch (e: ExposedSQLException) {
            ServiceResult.Failure(ErrorCode.DATABASE_ERROR)
        }
    }

    override suspend fun getGroupById(id: Long): ServiceResult<AppGroup> {
        data class TempGroup(val id: Long, val name: String, val user: AppUser)

        return try {
            dbQuery {
                val tempGroup = GroupEntity
                    .innerJoin(UserEntity)
                    .select { GroupEntity.id eq id }
                    .map {
                        TempGroup(
                            id = it[GroupEntity.id],
                            name = it[GroupEntity.name],
                            user = AppUser(
                                id = it[UserEntity.id],
                                name = it[UserEntity.name],
                                age = it[UserEntity.age]
                            )
                        )
                    }
                    .singleOrNull() ?: return@dbQuery ServiceResult.Failure(ErrorCode.GROUP_NOT_EXISTS)


                val members = GroupMembersEntity
                    .innerJoin(UserEntity)
                    .select { GroupMembersEntity.group eq tempGroup.id }
                    .map {
                        AppUser(
                            id = it[UserEntity.id],
                            name = it[UserEntity.name],
                            age = it[UserEntity.age]
                        )
                    }

                ServiceResult.Success(
                    AppGroup(
                        id = tempGroup.id,
                        name = tempGroup.name,
                        owner = tempGroup.user,
                        members = members
                    )
                )
            }
        } catch (e: ExposedSQLException) {
            ServiceResult.Failure(ErrorCode.DATABASE_ERROR)
        }
    }

    override suspend fun addUserToGroup(groupId: Long, userId: Long): ServiceResult<Boolean> {
        return try {
            dbQuery {
                GroupMembersEntity.insert {
                    it[user] = userId
                    it[group] = group
                }
                ServiceResult.Success(true)
            }
        } catch (e: ExposedSQLException) {
            ServiceResult.Failure(ErrorCode.DATABASE_ERROR)
        }
    }

    override suspend fun getAllAssignableUsers(groupId: Long): ServiceResult<List<AppUser>> {
        return try {
            tryInTransaction {
                val groupMembers = GroupMembersEntity
                    .join(UserEntity, JoinType.INNER) { GroupMembersEntity.group eq groupId }
                    .selectAll()
                    .map {
                        AppUser(
                            id = it[UserEntity.id],
                            name = it[UserEntity.name],
                            age = it[UserEntity.age]
                        )
                    }

                ServiceResult.Success(groupMembers)
            }
        } catch (e: ExposedSQLException) {
            ServiceResult.Failure(ErrorCode.DATABASE_ERROR)
        }
    }
}