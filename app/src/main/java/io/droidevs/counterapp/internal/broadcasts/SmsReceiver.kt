package io.droidevs.counterapp.internal.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.domain.system.SystemCounterType
import io.droidevs.counterapp.internal.worker.SystemEventWorker

@AndroidEntryPoint
class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val work = OneTimeWorkRequestBuilder<SystemEventWorker>()
                .setInputData(
                    workDataOf(
                        SystemEventWorker.COUNTER_KEY to SystemCounterType.SMS_RECEIVED.key
                    )
                )
                .build()

            WorkManager.getInstance(context).enqueue(work)
        }
    }
}
