package io.droidevs.counterapp.domain.trackers

interface DeviceUsageTracker {
    fun getScreenTime(): Int { return 0 }
    fun getPhoneUnlocks(): Int { return 0 }
    fun getNotificationsReceived(): Int { return 0 }
    fun getNotificationsCleared(): Int { return 0 }
}