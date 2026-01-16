package io.droidevs.counterapp.preference.display

import io.droidevs.counterapp.data.Theme
import io.droidevs.counterapp.domain.preference.display.ThemePreference
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.asSuccess
import io.droidevs.counterapp.domain.result.errors.PreferenceError
import io.droidevs.counterapp.preference.DummyPreferenceDelegates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DummyThemePreference(
    initialValue: Theme = Theme.SYSTEM   // common default
) : ThemePreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "app_theme",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<Result<Theme, PreferenceError>> = delegate.flow.map { it.asSuccess() }

    override suspend fun set(value: Theme): Result<Unit, PreferenceError> {
        delegate.set(value)
        return Unit.asSuccess()
    }

    // Optional: convenience method
    fun isDark(): Boolean = delegate.getCurrent() == Theme.DARK
}
