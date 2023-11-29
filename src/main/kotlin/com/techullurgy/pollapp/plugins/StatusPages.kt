package com.techullurgy.pollapp.plugins

import com.techullurgy.pollapp.exceptions.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when(cause) {
                is ExceptionWithErrorCode -> {
                    call.respond(status = HttpStatusCode.BadRequest, message = cause.localizedMessage)
                }

                is AuthenticationException -> {
                    call.respond(status = HttpStatusCode.Unauthorized, message = cause.localizedMessage)
                }

                is DatabaseException -> {
                    call.respond(message = "Sorry, Something went wrong", status = HttpStatusCode.InternalServerError)
                }
                else -> {
                    call.respond(message = "Sorry, Something went wrong", status = HttpStatusCode.InternalServerError)
                }
            }
        }
    }
}