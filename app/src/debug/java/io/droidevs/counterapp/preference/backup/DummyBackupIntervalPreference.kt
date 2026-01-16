package io.droidevs.counterapp.preference.backup


import io.droidevs.counterapp.domain.preference.buckup.BackupIntervalPreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.asSuccess
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import io.droidevs.counterapp.preference.DummyPreferenceDelegates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DummyBackupIntervalPreference(
    initialValue: Long = 24L   // once per day is a reasonable default
) : BackupIntervalPreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "backup_interval_hours",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<Result<Long, PreferenceError>> = delegate.flow.map { it.asSuccess() }

    override suspend fun set(value: Long): Result<Unit, PreferenceError> {
        delegate.set(value)
        return Unit.asSuccess()
    }

    // Optional convenience method
    fun getIntervalInHours(): Long = delegate.getCurrent()
}
