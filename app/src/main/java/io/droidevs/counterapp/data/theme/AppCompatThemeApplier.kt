package io.droidevs.counterapp.data.theme

import androidx.appcompat.app.AppCompatDelegate
import io.droidevs.counterapp.data.Theme
import io.droidevs.counterapp.domain.theme.ThemeApplier
import javax.inject.Inject

class AppCompatThemeApplier @Inject constructor() : ThemeApplier {
    override fun apply(theme: Theme) {
        when (theme) {
            Theme.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Theme.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Theme.AUTO -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
            Theme.SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}

