package io.droidevs.counterapp.preference.backup


import io.droidevs.counterapp.domain.preference.buckup.AutoBackupPreference
import io.droidevs.counterapp.preference.DummyPreferenceDelegates
import kotlinx.coroutines.flow.Flow

class DummyAutoBackupPreference(
    initialValue: Boolean = true   // auto-backup usually enabled by default
) : AutoBackupPreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "auto_backup_enabled",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<Boolean> = delegate.flow

    override suspend fun set(value: Boolean) {
        delegate.set(value)
    }
}