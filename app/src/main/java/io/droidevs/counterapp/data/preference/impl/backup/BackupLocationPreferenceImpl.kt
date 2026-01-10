package io.droidevs.counterapp.data.preference.impl.backup

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.droidevs.counterapp.domain.preference.buckup.BackupLocationPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class BackupLocationPreferenceImpl(
    private val dataStore: DataStore<Preferences>
) : BackupLocationPreference {

    companion object {
        val key = stringPreferencesKey("backup_location")

        // Common/default values
        const val DEFAULT_VALUE = "local"
        const val LOCAL = "local"
        const val GOOGLE_DRIVE = "google_drive"
        const val DROPBOX = "dropbox"
        const val ICLOUD = "icloud"
        const val OTHER_CLOUD = "other_cloud"
    }

    override fun get(): Flow<String> = dataStore.data
        .map { prefs -> prefs[key] ?: DEFAULT_VALUE }

    override suspend fun set(value: String) {
        dataStore.edit { prefs ->
            prefs[key] = value
        }
    }

    // ───────────────────────────────────────────────
    //            Convenience helper methods
    // ───────────────────────────────────────────────

    suspend fun getCurrent(): String = get().first()

    suspend fun isLocal(): Boolean = getCurrent() == LOCAL

    suspend fun isCloud(): Boolean =
        getCurrent() in listOf(GOOGLE_DRIVE, DROPBOX, ICLOUD, OTHER_CLOUD)

    suspend fun isGoogleDrive(): Boolean = getCurrent() == GOOGLE_DRIVE

    suspend fun isDropbox(): Boolean = getCurrent() == DROPBOX

    // Optional: useful when showing UI options
    suspend fun requiresAuthentication(): Boolean =
        getCurrent() in listOf(GOOGLE_DRIVE, DROPBOX, ICLOUD)
}