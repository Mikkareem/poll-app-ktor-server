package com.techullurgy.pollapp.auth

import com.techullurgy.pollapp.data.dao.SecurityDao
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject

fun AuthenticationConfig.jwtAuthenticationConfig(name: String) {
    jwt(name) {
        verifier(JwtConfiguration.verifier())
        validate {
            val securityDao by inject<SecurityDao>()

            if(securityDao.validateToken(this)) {
                JWTPrincipal(it.payload)
            } else {
                null
            }
//            if(it.payload.getClaim(JwtConfiguration.CLAIM_USER_ID).asString() != "") { JWTPrincipal(it.payload) } else { null }
        }
        challenge { defaultScheme, realm ->
            println("defaultScheme: $defaultScheme")
            println("realm: $realm")
            call.respond(message = "Unauthorized", status = HttpStatusCode.Unauthorized)
        }
    }
}