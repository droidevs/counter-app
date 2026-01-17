package io.droidevs.counterapp.internal.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.domain.system.SystemCounterType
import io.droidevs.counterapp.internal.system.ReceiverGuards
import io.droidevs.counterapp.internal.system.SystemCounterWork

@AndroidEntryPoint
class NewOutgoingCallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        @Suppress("DEPRECATION")
        if (intent.action != Intent.ACTION_NEW_OUTGOING_CALL) return
        if (!ReceiverGuards.outgoingCallBroadcastSupported(context)) return

        SystemCounterWork.enqueueIncrement(
            context = context,
            counterKey = SystemCounterType.CALLS_MADE.key
        )
    }
}