package io.droidevs.counterapp.domain.trackers

import android.content.Context

interface CommunicationTracker {

    fun getCallsMade(): Int { return 0 }
    fun getCallsReceived(): Int { return 0 }
    fun getSMSReceived(): Int { return 0 }

//    fun registerReceiver(context: Context) {}
//    fun unregisterReceiver(context: Context) {}
}