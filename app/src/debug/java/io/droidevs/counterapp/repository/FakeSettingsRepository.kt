package io.droidevs.counterapp.repository

import io.droidevs.counterapp.domain.repository.SettingsRepository

@Deprecated("Use a mock instance of SettingsRepository instead.")
class FakeSettingsRepository() : SettingsRepository {

    private val data = mutableMapOf<String, Any>()

    override fun getBoolean(key: String, default: Boolean): Boolean =
        data[key] as? Boolean ?: default

    override fun getString(key: String, default: String): String =
        data[key] as? String ?: default

    override fun setBoolean(key: String, value: Boolean) {
        data[key] = value
    }

    override fun setString(key: String, value: String) {
        data[key] = value
    }

    /* ---------- Helpers for tests ---------- */

    fun clear() {
        data.clear()
    }

    fun <K,V> seed(values: Map<K, V>) {
        data.putAll(values as Map<out String, Any>)
    }
}