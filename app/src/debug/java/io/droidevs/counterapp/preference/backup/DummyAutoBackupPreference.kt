package io.droidevs.counterapp.preference.backup


import io.droidevs.counterapp.domain.preference.buckup.AutoBackupPreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.asSuccess
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import io.droidevs.counterapp.preference.DummyPreferenceDelegates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DummyAutoBackupPreference(
    initialValue: Boolean = true   // auto-backup usually enabled by default
) : AutoBackupPreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "auto_backup_enabled",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<Result<Boolean, PreferenceError>> = delegate.flow.map { it.asSuccess() }

    override suspend fun set(value: Boolean): Result<Unit, PreferenceError> {
        delegate.set(value)
        return Unit.asSuccess()
    }
}
