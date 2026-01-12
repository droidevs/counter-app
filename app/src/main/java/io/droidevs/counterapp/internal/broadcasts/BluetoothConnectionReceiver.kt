package io.droidevs.counterapp.internal.broadcasts

import android.bluetooth.BluetoothAdapter
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
class BluetoothConnectionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED) {
            val connectionState = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, -1)
            if (connectionState == BluetoothAdapter.STATE_CONNECTED) {
                val work = OneTimeWorkRequestBuilder<SystemEventWorker>()
                    .setInputData(
                        workDataOf(
                            SystemEventWorker.COUNTER_KEY to SystemCounterType.BLUETOOTH_CONNECTIONS.key
                        )
                    )
                    .build()

                WorkManager.getInstance(context).enqueue(work)
            }
        }
    }
}