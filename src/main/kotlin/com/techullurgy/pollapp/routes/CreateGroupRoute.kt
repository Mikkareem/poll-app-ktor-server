package com.techullurgy.pollapp.routes

import com.techullurgy.pollapp.domain.usecases.CreateGroupUseCase
import com.techullurgy.pollapp.network.requests.CreateGroupRequest
import com.techullurgy.pollapp.network.responses.CreateGroupSuccessResponse
import com.techullurgy.pollapp.utils.ErrorCode
import com.techullurgy.pollapp.utils.ServiceResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.createGroup() {
    val createGroupUseCase by inject<CreateGroupUseCase>()

    post {
        val request = call.receiveNullable<CreateGroupRequest>() ?: ServiceResult.Failure(ErrorCode.INVALID_REQUEST).data()

        val groupId = createGroupUseCase(
            groupName = request.groupName,
            ownerId = request.ownerId
        ).data()

        call.respond(
            status = HttpStatusCode.Accepted,
            message = CreateGroupSuccessResponse(
                groupId = groupId,
                createdBy = request.ownerId
            )
        )
    }
}