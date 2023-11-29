package com.techullurgy.pollapp.routes

import com.techullurgy.pollapp.domain.usecases.AssignPollUseCase
import com.techullurgy.pollapp.network.requests.AssignPollRequest
import com.techullurgy.pollapp.network.responses.PollAssignResponse
import com.techullurgy.pollapp.utils.ErrorCode
import com.techullurgy.pollapp.utils.ServiceResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.assignPoll() {
    val assignPollUseCase by inject<AssignPollUseCase>()

    post {
        val assignableData = call.receiveNullable<AssignPollRequest>()
            ?: ServiceResult.Failure(ErrorCode.INVALID_REQUEST).data()

        val success = assignPollUseCase(
            pollId = assignableData.pollId,
            pollAssignType = assignableData.pollAssignType,
            assignId = assignableData.assignId
        ).data()

        call.respond(
            message = PollAssignResponse(
                success = success,
                assignedPollId = assignableData.pollId
            ),
            status = HttpStatusCode.Accepted
        )
    }
}