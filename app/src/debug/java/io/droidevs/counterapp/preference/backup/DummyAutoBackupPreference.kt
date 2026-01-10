package io.droidevs.counterapp.preference.backup

// 1. AutoBackupPreference
import io.droidevs.counterapp.data.preference.dummy.DummyPreferenceDelegates
import io.droidevs.counterapp.domain.preference.buckup.AutoBackupPreference
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