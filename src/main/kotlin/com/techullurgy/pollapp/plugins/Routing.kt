package com.techullurgy.pollapp.plugins

import com.techullurgy.pollapp.auth.AuthType
import com.techullurgy.pollapp.routes.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/register") { register() }
        route("/login") { login() }

        authenticate(AuthType.JWTAuth.value) {
            route("/polls/assigned") { getAssignedPollsForUser() }
            route("/poll/create") { createPoll() }
            route("/poll/assign") { assignPoll() }
            route("/poll/submit") { submitPollAnswers() }
            route("/poll/{pollId}") { getPollById() }

            route("/group/create") { createGroup() }
            route("/group/add") { addUserToGroup() }
            route("/group/{id}") { getGroupMembers() }
            route("/groups") { getUserGroups() }

            route("/logout") { logout() }
        }
    }
}
