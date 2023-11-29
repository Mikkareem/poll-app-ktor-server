package com.techullurgy.pollapp.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import com.techullurgy.pollapp.network.models.User
import java.time.Instant

class JwtConfiguration {
    fun createAuthToken(user: User): String {
        return JWT.create()
            .withClaim(CLAIM_USER_ID, user.id)
            .withAudience(AUDIENCE)
            .withIssuer(ISSUER)
            .withExpiresAt(Instant.ofEpochMilli(System.currentTimeMillis() + 5*60*1000))
            .sign(Algorithm.HMAC256(SECRET))
    }

    companion object {
        private const val SECRET = "POLL_APP"
        private const val AUDIENCE = "http://localhost:8080/routes"
        private const val ISSUER = "http://localhost:8080/"
        const val CLAIM_USER_ID = "user_id"


        fun verifier(): JWTVerifier = JWT
            .require(Algorithm.HMAC256(SECRET))
            .withAudience(AUDIENCE)
            .withIssuer(ISSUER)
            .build()
    }
}