package io.droidevs.counterapp.preference.backup


import io.droidevs.counterapp.domain.preference.buckup.BackupLocationPreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.asSuccess
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import io.droidevs.counterapp.preference.DummyPreferenceDelegates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DummyBackupLocationPreference(
    initialValue: String = "local"   // common values: "local", "google_drive", "dropbox", "icloud", etc.
) : BackupLocationPreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "backup_location",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<Result<String, PreferenceError>> = delegate.flow.map { it.asSuccess() }

    override suspend fun set(value: String): Result<Unit, PreferenceError> {
        delegate.set(value)
        return Unit.asSuccess()
    }

    // Optional: convenience helpers
    fun isLocal(): Boolean = delegate.getCurrent() == "local"
    fun isCloud(): Boolean = delegate.getCurrent().contains("drive") || delegate.getCurrent().contains("cloud")
}
