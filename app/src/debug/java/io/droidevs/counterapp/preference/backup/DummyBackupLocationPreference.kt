package io.droidevs.counterapp.preference.backup

// 3. BackupLocationPreference (String-based)
import io.droidevs.counterapp.data.preference.dummy.DummyPreferenceDelegates
import io.droidevs.counterapp.domain.preference.buckup.BackupLocationPreference
import kotlinx.coroutines.flow.Flow

class DummyBackupLocationPreference(
    initialValue: String = "local"   // common values: "local", "google_drive", "dropbox", "icloud", etc.
) : BackupLocationPreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "backup_location",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<String> = delegate.flow

    override suspend fun set(value: String) {
        delegate.set(value)
    }

    // Optional: convenience helpers
    fun isLocal(): Boolean = delegate.getCurrent() == "local"
    fun isCloud(): Boolean = delegate.getCurrent().contains("drive") || delegate.getCurrent().contains("cloud")
}