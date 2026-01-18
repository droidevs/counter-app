package io.droidevs.counterapp.domain.backup

/**
 * Auto-backup configuration, sourced from Backup preferences.
 *
 * @param enabled If false, any scheduled auto-backup should be cancelled.
 * @param intervalHours Period between backups. Must be >= 1.
 * @param location Where backups are stored (e.g. local app storage, cloud).
 */
data class BackupConfig(
    val enabled: Boolean,
    val intervalHours: Long,
    val location: BackupLocation
)

sealed class BackupLocation {
    data object Local : BackupLocation()

    /** Placeholder for future integration. */
    data object GoogleDrive : BackupLocation()

    companion object {
        fun fromPreferenceValue(value: String): BackupLocation = when (value.lowercase()) {
            "local" -> Local
            "google_drive" -> GoogleDrive
            else -> Local
        }
    }
}

