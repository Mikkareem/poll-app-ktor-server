package com.techullurgy.pollapp.routes

import com.techullurgy.pollapp.domain.usecases.CreateUserUseCase
import com.techullurgy.pollapp.utils.ErrorCode
import com.techullurgy.pollapp.utils.ServiceResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.register() {
    val createUserUseCase by inject<CreateUserUseCase>()

    post {
        val username = call.parameters["username"]?.let {
            if(it.isBlank()) {
                ServiceResult.Failure(ErrorCode.INVALID_USERNAME).data()
            }
            it
        } ?: ServiceResult.Failure(ErrorCode.INVALID_USERNAME).data()

        val password = call.parameters["password"]?.let {
            if(it.isBlank()) {
                ServiceResult.Failure(ErrorCode.INVALID_USERNAME).data()
            }
            it
        } ?: ServiceResult.Failure(ErrorCode.INVALID_USERNAME).data()

        val age = call.parameters["age"]?.let {
            try {
                it.toInt()
            } catch (e: NumberFormatException) {
                ServiceResult.Failure(ErrorCode.INVALID_AGE).data()
            }
        } ?: ServiceResult.Failure(ErrorCode.INVALID_AGE).data()

        val response = createUserUseCase(name = username, age = age, password = password).data()
        call.respond(message = response, status = HttpStatusCode.Accepted)
    }
}