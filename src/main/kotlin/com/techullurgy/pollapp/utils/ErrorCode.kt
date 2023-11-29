package com.techullurgy.pollapp.utils

enum class ErrorCode(val message: String) {
    DATABASE_ERROR("The unexpected database error occurs"),
    INVALID_REQUEST("Request is invalid"),

    GROUP_NOT_EXISTS("The requested group not exists"),
    USER_NOT_EXISTS("The requested user not exists"),

    INVALID_USERNAME("Username is invalid"),
    INVALID_AGE("User Age is invalid"),
    INVALID_PASSWORD("User password is invalid"),
    INVALID_POLL_NAME("Poll name is invalid"),
    INVALID_POLL_DESCRIPTION("Poll description is invalid"),

    AUTHENTICATION_FAILED("Authentication Failed")
}