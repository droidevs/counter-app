package io.droidevs.counterapp.data.preference.dummy.backup

// 2. BackupIntervalPreference (in hours)
import io.droidevs.counterapp.data.preference.dummy.DummyPreferenceDelegates
import io.droidevs.counterapp.domain.preference.buckup.BackupIntervalPreference
import kotlinx.coroutines.flow.Flow

class DummyBackupIntervalPreference(
    initialValue: Long = 24L   // once per day is a reasonable default
) : BackupIntervalPreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "backup_interval_hours",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<Long> = delegate.flow

    override suspend fun set(value: Long) {
        delegate.set(value)
    }

    // Optional convenience method
    fun getIntervalInHours(): Long = delegate.getCurrent()
}