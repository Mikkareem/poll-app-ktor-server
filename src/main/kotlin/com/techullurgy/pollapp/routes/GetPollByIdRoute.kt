package com.techullurgy.pollapp.routes

import com.techullurgy.pollapp.domain.usecases.GetPollByIdUseCase
import com.techullurgy.pollapp.utils.ErrorCode
import com.techullurgy.pollapp.utils.ServiceResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.getPollById() {
    val getPollByIdUseCase by inject<GetPollByIdUseCase>()

    get {
        val pollId = call.parameters["pollId"]?.let {
            try {
                it.toLong()
            } catch (e: NumberFormatException) {
                ServiceResult.Failure(ErrorCode.INVALID_REQUEST).data()
            }
        } ?: ServiceResult.Failure(ErrorCode.INVALID_REQUEST).data()

        val poll = getPollByIdUseCase(pollId).data().toPoll()
        call.respond(message = poll, status = HttpStatusCode.OK)
    }
}