package io.droidevs.counterapp.ui.display

import io.droidevs.counterapp.domain.coroutines.annotation.ApplicationScope
import io.droidevs.counterapp.domain.display.DisplayPreferences
import io.droidevs.counterapp.domain.display.DisplayPreferencesProvider
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.dataOr
import io.droidevs.counterapp.domain.result.onFailure
import io.droidevs.counterapp.domain.result.recoverWith
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * @deprecated Use [DisplayPreferencesProvider] directly. UI should observe the domain model and push it into adapters.
 */
@Deprecated("Use DisplayPreferencesProvider; UI should observe domain model and push values into adapters")
@Singleton
class DisplayPreferencesManager @Inject constructor(
    provider: DisplayPreferencesProvider,
    @ApplicationScope scope: CoroutineScope,
    errorHandler: DisplayPreferencesErrorHandler,
) {

    val preferences: StateFlow<DisplayPreferences> = provider.preferences()
        .onFailure { errorHandler.onError(it) }
        .recoverWith {
            Result.Success(
                DisplayPreferences(
                    hideControls = false,
                    hideLastUpdate = false,
                    hideCounterCategoryLabel = false
                )
            )
        }
        .map { it.dataOr { DisplayPreferences(false, false, false) } }
        .stateIn(scope, SharingStarted.Eagerly, DisplayPreferences(false, false, false))
}
