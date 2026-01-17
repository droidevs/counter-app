package io.droidevs.counterapp.internal.system

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Persists last-fired timestamps per system event to avoid accidental double counting.
 *
 * This is intentionally simple and process-safe:
 * - BroadcastReceiver can be invoked even when app process is cold-started.
 * - SharedPreferences is suitable for small key/value throttling.
 */
internal class SystemEventDebouncer(
    context: Context
) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    /**
     * @return true if the event should be accepted now (not throttled)
     */
    fun shouldAccept(eventKey: String, windowMs: Long, nowMs: Long = System.currentTimeMillis()): Boolean {
        if (windowMs <= 0L) return true

        val last = prefs.getLong(keyFor(eventKey), 0L)
        if (last != 0L && nowMs - last < windowMs) return false

        prefs.edit { putLong(keyFor(eventKey), nowMs) }
        return true
    }

    fun clear(eventKey: String) {
        prefs.edit { remove(keyFor(eventKey)) }
    }

    private fun keyFor(eventKey: String) = "last_$eventKey"

    private companion object {
        private const val PREF_NAME = "system_event_debounce"
    }
}

