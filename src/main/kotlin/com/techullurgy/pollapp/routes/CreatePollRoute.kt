package com.techullurgy.pollapp.routes

import com.techullurgy.pollapp.auth.JwtConfiguration
import com.techullurgy.pollapp.data.dao.PollsDao
import com.techullurgy.pollapp.domain.usecases.CreatePollUseCase
import com.techullurgy.pollapp.network.models.AssignablePoll
import com.techullurgy.pollapp.network.responses.CreatePollSuccessResponse
import com.techullurgy.pollapp.utils.ErrorCode
import com.techullurgy.pollapp.utils.ServiceResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.createPoll() {
    val createPollUseCase by inject<CreatePollUseCase>()

    post {
        val principal = call.principal<JWTPrincipal>()!!

        val currentUser = principal.payload.claims[JwtConfiguration.CLAIM_USER_ID].toString().toLong()

        val assignablePoll = call.receiveNullable<AssignablePoll>()
            ?: ServiceResult.Failure(ErrorCode.INVALID_REQUEST).data()

        val pollId = createPollUseCase(assignablePoll.toAppPoll()).data()

        call.respond(
            status = HttpStatusCode.Accepted,
            message = CreatePollSuccessResponse(
                pollId = pollId,
                createdBy = currentUser
            )
        )
    }
}