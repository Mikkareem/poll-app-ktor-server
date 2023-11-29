package com.techullurgy.pollapp.auth

import com.techullurgy.pollapp.data.dao.UsersDao
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject

fun AuthenticationConfig.formAuthenticationConfig(name: String) {
    form(name) {
        userParamName = "username"
        passwordParamName = "password"
        validate { credential ->
            val usersDao by inject<UsersDao>()

            val user = usersDao.authenticate(name = credential.name, password = credential.password).data()
            UserIdPrincipal(user.name)
        }
        challenge {
            call.respond(HttpStatusCode.Unauthorized, "Credentials are not valid")
        }
    }
}