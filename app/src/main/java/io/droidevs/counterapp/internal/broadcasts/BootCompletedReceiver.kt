package io.droidevs.counterapp.internal.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.domain.system.SystemCounterType
import io.droidevs.counterapp.internal.system.SystemCounterWork

@AndroidEntryPoint
class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_LOCKED_BOOT_COMPLETED -> {
                SystemCounterWork.enqueueIncrement(
                    context = context,
                    counterKey = SystemCounterType.DEVICE_RESTARTS.key,
                    // boot should never be spammed, but keep unique work.
                    unique = true,
                    debounceWindowMs = 0L
                )
            }
        }
    }
}
