package io.droidevs.counterapp.internal.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ConnectivityReceiver(
    private val onNetworkEvent:() -> Unit
) : BroadcastReceiver(){

    override fun onReceive(p0: Context?, p1: Intent?) {
        onNetworkEvent()
    }
}