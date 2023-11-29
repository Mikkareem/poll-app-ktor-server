package com.techullurgy.pollapp.routes

import com.techullurgy.pollapp.auth.JwtConfiguration
import com.techullurgy.pollapp.data.dao.SecurityDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.logout() {
    val securityDao by inject<SecurityDao>()

    post {
        val principal = call.principal<JWTPrincipal>()!!
        val currentUser = principal.payload.claims[JwtConfiguration.CLAIM_USER_ID].toString().toLong()
        securityDao.logoutUser(userId = currentUser)
        call.respond(status = HttpStatusCode.Accepted, message = "Success")
    }
}