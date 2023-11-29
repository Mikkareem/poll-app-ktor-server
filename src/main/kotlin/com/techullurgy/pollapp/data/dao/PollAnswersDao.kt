package com.techullurgy.pollapp.data.dao

import com.techullurgy.pollapp.data.tables.PollAnswerDetailsEntity
import com.techullurgy.pollapp.data.tables.PollAnswersEntity
import com.techullurgy.pollapp.domain.models.AnsweredAppPoll
import com.techullurgy.pollapp.plugins.dbQuery
import com.techullurgy.pollapp.utils.ErrorCode
import com.techullurgy.pollapp.utils.ServiceResult
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

interface PollAnswersDao {
    suspend fun answered(answeredAppPoll: AnsweredAppPoll, userId: Long): ServiceResult<Boolean>

    suspend fun getAnsweredPollForUser(userId: Long, pollId: Long): ServiceResult<AnsweredAppPoll>
}

internal class PollAnswersDaoImpl(
    private val pollsDao: PollsDao
): PollAnswersDao {
    override suspend fun answered(answeredAppPoll: AnsweredAppPoll, userId: Long): ServiceResult<Boolean> {
        return try {
            val pollId = answeredAppPoll.appPoll.id
            val questions = answeredAppPoll.appPoll.questions.map { it.id }
            val answers = answeredAppPoll.answers

            require(questions.size == answers.size)

            dbQuery {
                val pollAnswerId = PollAnswersEntity.insert {
                    it[this.pollId] = pollId
                    it[this.attenderId] = userId
                }[PollAnswersEntity.id]

                for (index in questions.indices) {
                    val questionId = questions[index]
                    val answerId = answers[index]

                    PollAnswerDetailsEntity.insert {
                        it[this.pollAnswerId] = pollAnswerId
                        it[this.questionId] = questionId
                        it[this.selectedOption] = answerId
                    }
                }

                ServiceResult.Success(true)
            }
        } catch (e: ExposedSQLException) {
            ServiceResult.Failure(ErrorCode.DATABASE_ERROR)
        }
    }

    override suspend fun getAnsweredPollForUser(userId: Long, pollId: Long): ServiceResult<AnsweredAppPoll> {
        return try {
            dbQuery {
                val answers = PollAnswersEntity
                    .innerJoin(PollAnswerDetailsEntity)
                    .select { (PollAnswersEntity.pollId eq pollId) and (PollAnswersEntity.attenderId eq userId) }
                    .map {
                        it[PollAnswerDetailsEntity.selectedOption]
                    }

                val appPoll = pollsDao.getPollDetailsById(pollId = pollId).data()

                ServiceResult.Success(
                    AnsweredAppPoll(
                        appPoll = appPoll,
                        answers = answers
                    )
                )
            }
        } catch (e: ExposedSQLException) {
            ServiceResult.Failure(ErrorCode.DATABASE_ERROR)
        }
    }
}