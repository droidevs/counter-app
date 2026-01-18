package io.droidevs.counterapp.internal.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.domain.system.SystemCounterType
import io.droidevs.counterapp.internal.system.SystemCounterWork

@AndroidEntryPoint
class PowerConnectionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_POWER_CONNECTED,
            Intent.ACTION_POWER_DISCONNECTED -> {
                SystemCounterWork.enqueueIncrement(
                    context = context,
                    counterKey = SystemCounterType.BATTERY_CHARGES.key,
                    unique = false,
                    debounceWindowMs = 0L
                )
            }
        }
    }
}
