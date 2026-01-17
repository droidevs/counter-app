package io.droidevs.counterapp.ui.system

import android.content.Context
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.system.SystemCounterType

object SystemCounterSupportUi {

    fun hintText(context: Context, systemKey: String?): String? {
        val key = systemKey ?: return null
        val type = SystemCounterType.values().firstOrNull { it.key == key } ?: return null

        // Keep the chip short. Use the system counter's display name.
        return context.getString(R.string.system_counter_hint_generic, type.displayName)
    }
}
