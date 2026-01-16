package io.droidevs.counterapp.domain.repository

@Deprecated("Use UseCases instead of SettingsRepository directly.")
interface SettingsRepository {

    /* ---------- Read ---------- */

    fun getBoolean(key: String, default: Boolean): Boolean
    fun getString(key: String, default: String): String

    /* ---------- Write ---------- */

    fun setBoolean(key: String, value: Boolean)
    fun setString(key: String, value: String)
}
