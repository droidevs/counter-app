package io.droidevs.counterapp.internal.broadcasts

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.domain.system.SystemCounterType
import io.droidevs.counterapp.internal.system.ReceiverGuards
import io.droidevs.counterapp.internal.system.SystemCounterWork

@AndroidEntryPoint
class BluetoothConnectionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (!ReceiverGuards.hasBluetoothConnectIfNeeded(context)) return

        if (intent.action == BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED) {
            val connectionState = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, -1)
            if (connectionState == BluetoothAdapter.STATE_CONNECTED) {
                SystemCounterWork.enqueueIncrement(
                    context = context,
                    counterKey = SystemCounterType.BLUETOOTH_CONNECTIONS.key
                )
            }
        }
    }
}