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
        if (intent.action != DownloadManager.ACTION_DOWNLOAD_COMPLETE) return

        val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
        val eventId = if (downloadId > 0L) downloadId.toString() else null

        SystemCounterWork.enqueueIncrement(
            context = context,
            counterKey = SystemCounterType.FILES_DOWNLOADED.key,
            // de-bounce duplicates for the same downloadId, but allow many downloads quickly.
            debounceWindowMs = 60_000L,
            eventId = eventId
        )
    }
}