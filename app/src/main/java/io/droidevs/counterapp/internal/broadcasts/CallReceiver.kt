package io.droidevs.counterapp.internal.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.domain.system.SystemCounterType
import io.droidevs.counterapp.internal.worker.SystemEventWorker

@AndroidEntryPoint
class CallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                val work = OneTimeWorkRequestBuilder<SystemEventWorker>()
                    .setInputData(
                        workDataOf(
                            SystemEventWorker.COUNTER_KEY to SystemCounterType.CALLS_RECEIVED.key
                        )
                    )
                    .build()

                WorkManager.getInstance(context).enqueue(work)
            }
        }
    }
}
