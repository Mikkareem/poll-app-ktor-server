package com.techullurgy.pollapp.data.dao

import com.auth0.jwt.JWT
import com.techullurgy.pollapp.auth.JwtConfiguration
import com.techullurgy.pollapp.data.tables.UserSecurity
import com.techullurgy.pollapp.plugins.dbQuery
import io.ktor.server.application.*
import io.ktor.server.auth.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

interface SecurityDao {
    suspend fun validateToken(applicationCall: ApplicationCall): Boolean

    suspend fun loginUserWithToken(userId: Long, token: String)

    suspend fun logoutUser(userId: Long)
}

internal class SecurityDaoImpl: SecurityDao {
    override suspend fun validateToken(applicationCall: ApplicationCall): Boolean {
        val token = applicationCall.authentication.call.request.headers["Authorization"]?.split(" ")?.get(1) ?: return false

        val decodedJWT = JWT.decode(token)
        if(decodedJWT.expiresAtAsInstant.toEpochMilli() < System.currentTimeMillis()) return false

        val userId = decodedJWT.claims[JwtConfiguration.CLAIM_USER_ID].toString().toLong()
        val userToken = dbQuery {
            UserSecurity.select { UserSecurity.user eq userId }.map { it[UserSecurity.token] }.singleOrNull()
        } ?: return false

        return userToken == token
    }

    override suspend fun loginUserWithToken(userId: Long, token: String) {
        dbQuery {
            val count = UserSecurity.update(where = { UserSecurity.user eq userId }) { it[this.token] = token }
            if(count < 1) {
                UserSecurity.insert {
                    it[user] = userId
                    it[this.token] = token
                }
            }
        }
    }

    override suspend fun logoutUser(userId: Long) {
        dbQuery { UserSecurity.deleteWhere { user.eq(userId) } }
    }
}