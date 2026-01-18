package io.droidevs.counterapp.internal.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.domain.system.SystemCounterType
import io.droidevs.counterapp.internal.system.ReceiverGuards
import io.droidevs.counterapp.internal.system.SystemCounterWork

@AndroidEntryPoint
class ConnectivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (!ReceiverGuards.canCheckNetwork(context)) return

        val action = intent.action

        val isConnectedToWifi = when (action) {
            WifiManager.NETWORK_STATE_CHANGED_ACTION -> {
                @Suppress("DEPRECATION")
                val info = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
                @Suppress("DEPRECATION")
                info?.isConnected == true
            }

            WifiManager.WIFI_STATE_CHANGED_ACTION -> {
                val state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)
                state == WifiManager.WIFI_STATE_ENABLED
            }

            else -> {
                // Best-effort fallback: check active network.
                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val network = cm.activeNetwork ?: return
                    val caps = cm.getNetworkCapabilities(network) ?: return
                    caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                } else {
                    @Suppress("DEPRECATION")
                    cm.activeNetworkInfo?.type == ConnectivityManager.TYPE_WIFI
                }
            }
        }

        if (isConnectedToWifi) {
            SystemCounterWork.enqueueIncrement(
                context = context,
                counterKey = SystemCounterType.WIFI_CONNECTIONS.key
            )
        }
    }
}