package com.techullurgy.pollapp.plugins

import com.techullurgy.pollapp.auth.AuthType
import com.techullurgy.pollapp.auth.formAuthenticationConfig
import com.techullurgy.pollapp.auth.jwtAuthenticationConfig
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureSecurity() {
    install(Authentication) {
        formAuthenticationConfig(AuthType.FormAuth.value)
        jwtAuthenticationConfig(AuthType.JWTAuth.value)
    }
}
