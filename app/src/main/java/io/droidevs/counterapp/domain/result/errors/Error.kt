package io.droidevs.counterapp.domain.result.errors

import io.droidevs.counterapp.domain.result.RootError

sealed interface Error


data class InternalError(val message: String) : RootError

data class UnknownError(val message: String) : RootError