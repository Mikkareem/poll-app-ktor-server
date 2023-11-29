package com.techullurgy.pollapp.auth

sealed class AuthType(val value: String) {
    data object FormAuth: AuthType("auth-form")
    data object JWTAuth: AuthType("auth-jwt")
}