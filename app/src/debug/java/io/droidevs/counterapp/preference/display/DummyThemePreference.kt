package io.droidevs.counterapp.preference.display

import io.droidevs.counterapp.data.Theme
import io.droidevs.counterapp.domain.preference.display.ThemePreference
import io.droidevs.counterapp.preference.DummyPreferenceDelegates
import kotlinx.coroutines.flow.Flow

class DummyThemePreference(
    initialValue: Theme = Theme.SYSTEM   // common default
) : ThemePreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "app_theme",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<Theme> = delegate.flow

    override suspend fun set(value: Theme) {
        delegate.set(value)
    }

    // Optional: convenience method
    fun isDark(): Boolean = delegate.getCurrent() == Theme.DARK
}