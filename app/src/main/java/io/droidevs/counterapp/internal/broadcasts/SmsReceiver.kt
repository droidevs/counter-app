package io.droidevs.counterapp.internal.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.domain.system.SystemCounterType
import io.droidevs.counterapp.internal.system.ReceiverGuards
import io.droidevs.counterapp.internal.system.SystemCounterWork

@AndroidEntryPoint
class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (!ReceiverGuards.hasReceiveSms(context)) return

        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            SystemCounterWork.enqueueIncrement(
                context = context,
                counterKey = SystemCounterType.SMS_RECEIVED.key
            )
        }
    }
}
