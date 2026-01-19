package io.droidevs.counterapp.domain.display

import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.flow.Flow

/**
 * Global domain-level provider for display preferences.
 *
 * UI observes [preferences] and pushes the values down to adapters.
 */
interface DisplayPreferencesProvider {
    fun preferences(): Flow<Result<DisplayPreferences, PreferenceError>>
}

