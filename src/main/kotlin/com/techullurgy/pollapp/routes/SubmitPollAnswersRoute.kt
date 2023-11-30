package com.techullurgy.pollapp.routes

import com.techullurgy.pollapp.auth.JwtConfiguration
import com.techullurgy.pollapp.domain.usecases.SubmitPollAnswersUseCase
import com.techullurgy.pollapp.network.requests.SubmitPollAnswersRequest
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

fun Route.submitPollAnswers() {
    val submitPollAnswersUseCase by inject<SubmitPollAnswersUseCase>()

    post {

        val request = call.receiveNullable<SubmitPollAnswersRequest>() ?: ServiceResult.Failure(ErrorCode.INVALID_REQUEST).data()
        val answeredPoll = request.answeredPoll

        val principal = call.principal<JWTPrincipal>()!!
        val currentUser = principal.payload.claims[JwtConfiguration.CLAIM_USER_ID].toString().toLong()

        val success = submitPollAnswersUseCase.invoke(answeredPoll = answeredPoll, userId = currentUser)

        call.respond(
            status = if (success) HttpStatusCode.Accepted else HttpStatusCode.NotAcceptable,
            message = if (success) "success" else "failed"
        )
    }
}