package io.droidevs.counterapp.internal.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.domain.system.SystemCounterType
import io.droidevs.counterapp.internal.worker.SystemEventWorker

@AndroidEntryPoint
class NewOutgoingCallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_NEW_OUTGOING_CALL) {
            val work = OneTimeWorkRequestBuilder<SystemEventWorker>()
                .setInputData(
                    workDataOf(
                        SystemEventWorker.COUNTER_KEY to SystemCounterType.CALLS_MADE.key
                    )
                )
                .build()

            WorkManager.getInstance(context).enqueue(work)
        }
    }
}