package io.droidevs.counterapp.domain.trackers


interface NetworkConnectivityTracker {
    fun getWiFiConnections(): Int { return 0 }
    fun getBluetoothConnections(): Int { return 0 }
    fun getMobileDataUsage(): Int { return 0 }

//    fun registerReceiver(context: Context) {}
//    fun unregisterReceiver(context: Context) {}
}