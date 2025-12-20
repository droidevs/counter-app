package io.droidevs.counterapp.data.repository

import android.content.SharedPreferences
import io.droidevs.counterapp.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val prefs: SharedPreferences
) : SettingsRepository {

    override fun getBoolean(key: String, default: Boolean): Boolean =
        prefs.getBoolean(key, default)

    override fun getString(key: String, default: String): String =
        prefs.getString(key, default) ?: default

    override fun setBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    override fun setString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }
}
