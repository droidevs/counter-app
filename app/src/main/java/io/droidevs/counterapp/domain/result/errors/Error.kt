package io.droidevs.counterapp.domain.result.errors

import io.droidevs.counterapp.domain.result.RootError


interface Error


data class InternalError(val message: String) : RootError

data class UnknownError(val message: String) : RootError