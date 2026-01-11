package io.droidevs.counterapp.data

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.repository.SettingsRepository

enum class Theme(private val identifier: String, val labelId: Int) {
    LIGHT("light", R.string.settings_theme_light),
    DARK("dark", R.string.settings_theme_dark),
    AUTO("auto", R.string.settings_theme_auto_battery),
    SYSTEM("system", R.string.settings_theme_system);

    companion object {
        fun getCurrent(id: String): Theme {
            for (t in entries) {
                if (t.identifier == id) {
                    return t
                }
            }
            return SYSTEM
        }

        /**
         * Sets up current theme.
         *
         * @param sharedPrefs [SharedPreferences] that contain the theme preference.
         */
        fun initCurrentTheme(identifier: String) {
            val currentTheme = getCurrent(identifier)

            when (currentTheme) {
                LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                AUTO -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }
}