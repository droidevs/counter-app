package io.droidevs.counterapp.preference.backup


import io.droidevs.counterapp.domain.preference.buckup.BackupIntervalPreference
import io.droidevs.counterapp.preference.DummyPreferenceDelegates
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