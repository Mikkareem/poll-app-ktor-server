package com.techullurgy.pollapp.data.dao

import com.techullurgy.pollapp.data.tables.*
import com.techullurgy.pollapp.domain.models.AppPoll
import com.techullurgy.pollapp.domain.models.AppPollOption
import com.techullurgy.pollapp.domain.models.AppPollQuestion
import com.techullurgy.pollapp.network.models.PollAssignType
import com.techullurgy.pollapp.plugins.dbQuery
import com.techullurgy.pollapp.plugins.tryInTransaction
import com.techullurgy.pollapp.utils.ErrorCode
import com.techullurgy.pollapp.utils.ServiceResult
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*

interface PollsDao {
    suspend fun create(poll: AppPoll): ServiceResult<Long>

    // TODO: Handle assign poll itself to the owner, currently it assigns to owner itself if he/she is part of the group
    suspend fun assign(pollId: Long, assignType: PollAssignType, assignId: Long): ServiceResult<Boolean>

    suspend fun getAllAssignedPollsForUser(userId: Long): ServiceResult<List<AppPoll>>

    suspend fun getPollDetailsById(pollId: Long): ServiceResult<AppPoll>
}

internal class PollsDaoImpl(
    private val groupsDao: GroupsDao
): PollsDao {
    override suspend fun create(poll: AppPoll): ServiceResult<Long> {
        return try {
            dbQuery {
                val pollId = PollEntity.insert {
                    it[title] = poll.name
                    it[description] = poll.description
                }[PollEntity.id]

                poll.questions.forEach { question ->
                    val questionId = PollQuestionEntity.insert {
                        it[questionTitle] = question.question
                        it[this.pollId] = pollId
                    }[PollQuestionEntity.id]

                    question.options.forEach { option ->
                        PollOptionsEntity.insert {
                            it[optionIndex] = option.optionIndex
                            it[this.option] = option.optionName
                            it[this.questionId] = questionId
                        }
                    }
                }
                ServiceResult.Success(pollId)
            }
        } catch (e: ExposedSQLException) {
            ServiceResult.Failure(ErrorCode.DATABASE_ERROR)
        }
    }

    override suspend fun assign(pollId: Long, assignType: PollAssignType, assignId: Long): ServiceResult<Boolean> {
        return try {
            dbQuery {
                val pollAssignmentId = PollAssignmentsEntity.insert {
                    it[poll] = pollId
                    it[assignmentType] = assignType
                    it[assignedId] = assignId
                }[PollAssignmentsEntity.id]

                if(assignType == PollAssignType.GROUP) {
                    val assignableUsers = groupsDao.getAllAssignableUsers(groupId = assignId).data()
                    assignableUsers.forEach { user ->
                        PollAssignmentDetailsEntity.insert {
                            it[userId] = user.id
                            it[this.pollAssignmentId] = pollAssignmentId
                        }
                    }
                } else {
                    PollAssignmentDetailsEntity.insert {
                        it[userId] = assignId
                        it[this.pollAssignmentId] = pollAssignmentId
                    }
                }
                ServiceResult.Success(true)
            }
        } catch (e: ExposedSQLException) {
            ServiceResult.Failure(ErrorCode.DATABASE_ERROR)
        }
    }

    override suspend fun getAllAssignedPollsForUser(userId: Long): ServiceResult<List<AppPoll>> {
        return try {
            dbQuery {
                val polls = PollAssignmentDetailsEntity
                    .innerJoin(PollAssignmentsEntity)
                    .slice(PollAssignmentsEntity.poll)
                    .select { PollAssignmentDetailsEntity.userId eq userId }
                    .withDistinct()
                    .map { it[PollAssignmentsEntity.poll] }

                val assignedPolls = polls.map { getPollDetailsById(it).data() }

                ServiceResult.Success(assignedPolls)
            }
        } catch (e: ExposedSQLException) {
            ServiceResult.Failure(ErrorCode.DATABASE_ERROR)
        }
    }

    override suspend fun getPollDetailsById(pollId: Long): ServiceResult<AppPoll> {
        return try {
            tryInTransaction {
                val tempRecords = PollEntity
                    .innerJoin(PollQuestionEntity)
                    .innerJoin(PollOptionsEntity, onColumn = { PollQuestionEntity.id }, otherColumn = { questionId })
                    .select { PollEntity.id eq pollId }
                    .map { it.toTempPollRecords() }
                    .toAppPolls()
                    .singleOrNull() ?: ServiceResult.Failure(ErrorCode.INVALID_REQUEST).data()

                ServiceResult.Success(tempRecords)
            }
        } catch (e: ExposedSQLException) {
            ServiceResult.Failure(ErrorCode.DATABASE_ERROR)
        }
    }

    private fun ResultRow.toTempPollRecords(): TempPollRecord {
        val pollId = this[PollEntity.id]
        val pollName = this[PollEntity.title]
        val pollDesc = this[PollEntity.description]
        val questionId = this[PollQuestionEntity.id]
        val question = this[PollQuestionEntity.questionTitle]
        val questionPollId = this[PollQuestionEntity.pollId]
        val optionId = this[PollOptionsEntity.id]
        val optionName = this[PollOptionsEntity.option]
        val optionIndex = this[PollOptionsEntity.optionIndex]
        val optionQuestionId = this[PollOptionsEntity.questionId]

        return TempPollRecord(pollId, pollName, pollDesc, questionId, question, questionPollId, optionId, optionName, optionIndex, optionQuestionId)
    }

    private fun List<TempPollRecord>.toAppPolls(): List<AppPoll> {
        return groupBy { it.pollId }
            .map { poll ->
                val pollId = poll.value.groupBy { it.pollId }.keys.map { it }[0]
                val pollName = poll.value.groupBy { it.pollName }.keys.map { it }[0]
                val pollDesc = poll.value.groupBy { it.pollDesc }.keys.map { it }[0]
                val questions = poll.value.groupBy { it.questionId }.map { question ->
                    val questionId = question.value.groupBy { it.questionId }.keys.map { it }[0]
                    val questionName = question.value.groupBy { it.question }.keys.map { it }[0]
                    val options = question.value.map { option ->
                        AppPollOption(id = option.optionId, optionName = option.optionName, optionIndex = option.optionIndex)
                    }
                    AppPollQuestion(id = questionId, question = questionName, options = options)
                }
                AppPoll(pollId, pollName, pollDesc, questions)
            }
    }
}

data class TempPollRecord(
    val pollId: Long,
    val pollName: String,
    val pollDesc: String,
    val questionId: Long,
    val question: String,
    val questionPollId: Long,
    val optionId: Long,
    val optionName: String,
    val optionIndex: Int,
    val optionQuestionId: Long
) {
    fun toAppPollOption(): AppPollOption = AppPollOption(
        id = optionId,
        optionName = optionName,
        optionIndex = optionIndex
    )
}