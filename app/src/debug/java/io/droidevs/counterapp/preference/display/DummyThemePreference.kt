package io.droidevs.counterapp.preference.display

import io.droidevs.counterapp.data.preference.dummy.DummyPreferenceDelegates
import io.droidevs.counterapp.domain.preference.display.ThemePreference
import kotlinx.coroutines.flow.Flow

class DummyThemePreference(
    initialValue: String = "system"   // common default
) : ThemePreference {

    private val delegate by lazy {
        DummyPreferenceDelegates.getOrCreate(
            key = "app_theme",
            initialValue = initialValue
        )
    }

    override fun get(): Flow<String> = delegate.flow

    override suspend fun set(value: String) {
        delegate.set(value)
    }

    // Optional: convenience method
    fun isDark(): Boolean = delegate.getCurrent() == "dark"
}