package com.techullurgy.pollapp.routes

import com.techullurgy.pollapp.auth.JwtConfiguration
import com.techullurgy.pollapp.domain.usecases.GetUserGroupsUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.getUserGroups() {
    val getUserGroupsUseCase by inject<GetUserGroupsUseCase>()

    get {
        val principal = call.principal<JWTPrincipal>()!!
        val currentUser = principal.payload.claims[JwtConfiguration.CLAIM_USER_ID].toString().toLong()
        val groups = getUserGroupsUseCase(userId = currentUser)
        call.respond(
            message = groups,
            status = HttpStatusCode.OK
        )
    }
}