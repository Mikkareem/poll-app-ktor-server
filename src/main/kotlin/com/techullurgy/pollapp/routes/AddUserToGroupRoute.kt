package com.techullurgy.pollapp.routes

import com.techullurgy.pollapp.domain.usecases.AddMemberToGroupUseCase
import com.techullurgy.pollapp.network.requests.AddUserToGroupRequest
import com.techullurgy.pollapp.utils.ErrorCode
import com.techullurgy.pollapp.utils.ServiceResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.addUserToGroup() {
    val addMemberToGroupUseCase by inject<AddMemberToGroupUseCase>()

    post {
        val request = call.receiveNullable<AddUserToGroupRequest>() ?: ServiceResult.Failure(ErrorCode.INVALID_REQUEST).data()

        val groupId = request.groupId
        val userId = request.userId

        val success = addMemberToGroupUseCase(groupId = groupId, userId = userId).data()

        call.respond(
            status = if (success) HttpStatusCode.Accepted else HttpStatusCode.NotAcceptable,
            message = if (success) "success" else "failed"
        )
    }
}