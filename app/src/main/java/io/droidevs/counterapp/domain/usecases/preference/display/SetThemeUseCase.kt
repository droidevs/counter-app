package io.droidevs.counterapp.domain.usecases.preference.display

import io.droidevs.counterapp.data.Theme
import io.droidevs.counterapp.domain.coroutines.DispatcherProvider
import io.droidevs.counterapp.domain.preference.display.ThemePreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetThemeUseCase @Inject constructor(
    private val pref: ThemePreference,
    private val dispatchers: DispatcherProvider
) {
    suspend operator fun invoke(value: Theme): Result<Unit, PreferenceError> = withContext(dispatchers.io) {
        pref.set(value)
    }
}
