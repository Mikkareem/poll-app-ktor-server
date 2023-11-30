package com.techullurgy.pollapp.routes

import com.techullurgy.pollapp.domain.usecases.GetGroupMembersUseCase
import com.techullurgy.pollapp.utils.ErrorCode
import com.techullurgy.pollapp.utils.ServiceResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.getGroupMembers() {
    val getGroupMembersUseCase by inject<GetGroupMembersUseCase>()

    get {
        val groupId = call.parameters["id"]!!.let {
            try {
                it.toLong()
            } catch (e: NumberFormatException) {
                ServiceResult.Failure(ErrorCode.INVALID_REQUEST).data()
            }
        }

        val members = getGroupMembersUseCase(groupId = groupId).data()
        call.respond(
            message = members,
            status = HttpStatusCode.OK
        )
    }
}