package io.droidevs.counterapp.domain.repository

import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.DatabaseError

interface DataInitializer {
    suspend fun init(): Result<Unit, DatabaseError>
}
