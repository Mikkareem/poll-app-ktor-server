package com.techullurgy.pollapp.routes

import com.techullurgy.pollapp.auth.JwtConfiguration
import com.techullurgy.pollapp.domain.usecases.GetAssignedPollsForUserUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.getAssignedPollsForUser() {
    val getAssignedPollsForUserUseCase by inject<GetAssignedPollsForUserUseCase>()

    get {
        val principal = call.principal<JWTPrincipal>()!!
        val currentUser = principal.payload.claims[JwtConfiguration.CLAIM_USER_ID].toString().toLong()
        val assignedPolls = getAssignedPollsForUserUseCase(userId = currentUser)

        call.respond(message = assignedPolls, status = HttpStatusCode.OK)
    }
}