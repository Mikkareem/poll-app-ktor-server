package com.techullurgy.pollapp.utils

import com.techullurgy.pollapp.exceptions.*

sealed class ServiceResult<out T> {
    data class Success<T>(val data: T): ServiceResult<T>()
    data class Failure(val errorCode: ErrorCode): ServiceResult<Nothing>()

    fun data(): T {
        return when(this) {
            is Success -> data
            is Failure -> throwException(errorCode)
        }
    }

    private fun throwException(errorCode: ErrorCode): Nothing {
        throw when(errorCode) {
            ErrorCode.USER_NOT_EXISTS -> UserNotFoundException()
            ErrorCode.GROUP_NOT_EXISTS -> GroupNotFoundException()
            ErrorCode.INVALID_USERNAME -> InvalidUsernameException()
            ErrorCode.INVALID_AGE -> InvalidUserAgeException()
            ErrorCode.INVALID_PASSWORD -> InvalidUserPasswordException()
            ErrorCode.INVALID_POLL_NAME -> InvalidPollNameException()
            ErrorCode.INVALID_POLL_DESCRIPTION -> InvalidPollDescriptionException()
            ErrorCode.INVALID_REQUEST -> InvalidRequestException()
            else -> Exception("Unknown Exception")
        }
    }
}