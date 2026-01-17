package io.droidevs.counterapp.internal.broadcasts

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.domain.system.SystemCounterType
import io.droidevs.counterapp.internal.system.SystemCounterWork

@AndroidEntryPoint
class DownloadCompleteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
            SystemCounterWork.enqueueIncrement(
                context = context,
                counterKey = SystemCounterType.FILES_DOWNLOADED.key
            )
        }
    }
}