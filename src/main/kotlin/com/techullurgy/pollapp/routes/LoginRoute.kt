package com.techullurgy.pollapp.routes

import com.techullurgy.pollapp.auth.JwtConfiguration
import com.techullurgy.pollapp.data.dao.SecurityDao
import com.techullurgy.pollapp.domain.usecases.LoginUserUseCase
import com.techullurgy.pollapp.network.responses.LoginSuccessResponse
import com.techullurgy.pollapp.utils.ErrorCode
import com.techullurgy.pollapp.utils.ServiceResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.login() {
    val loginUserUseCase by inject<LoginUserUseCase>()
    val jwtConfig by inject<JwtConfiguration>()
    val securityDao by inject<SecurityDao>()

    post {
        val username = call.parameters["username"]?.let {
            if(it.isBlank()) {
                ServiceResult.Failure(ErrorCode.INVALID_USERNAME).data()
            }
            it
        } ?: ServiceResult.Failure(ErrorCode.INVALID_USERNAME).data()

        val password = call.parameters["password"]?.let {
            if(it.isBlank()) {
                ServiceResult.Failure(ErrorCode.INVALID_PASSWORD).data()
            }
            it
        } ?: ServiceResult.Failure(ErrorCode.INVALID_PASSWORD).data()

        val user = loginUserUseCase(username = username, password = password).data()

        val token = jwtConfig.createAuthToken(user)

        securityDao.loginUserWithToken(userId = user.id, token = token)

        call.respond(
            status = HttpStatusCode.OK,
            message = LoginSuccessResponse(
                success = true,
                userId = user.id,
                token = token
            )
        )
    }
}