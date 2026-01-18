package io.droidevs.counterapp.domain.permissions

import io.droidevs.counterapp.domain.result.errors.Error

sealed class PermissionError : Error {
    data class Internal(val message: String) : PermissionError()
}