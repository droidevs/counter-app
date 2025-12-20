package io.droidevs.counterapp.domain.system

import io.droidevs.counterapp.domain.trackers.ActivityTracker
import io.droidevs.counterapp.domain.trackers.BatteryPowerTracker
import io.droidevs.counterapp.domain.trackers.CommunicationTracker
import io.droidevs.counterapp.domain.trackers.DeviceUsageTracker
import io.droidevs.counterapp.domain.trackers.MediaStorageTracker
import io.droidevs.counterapp.domain.trackers.NetworkConnectivityTracker

class SystemCounterManagerImpl(
    private val activityTracker: ActivityTracker,
    private val deviceUsageTracker: DeviceUsageTracker,
    private val communicationTracker: CommunicationTracker,
    private val mediaStorageTracker: MediaStorageTracker,
    private val networkConnectivityTracker: NetworkConnectivityTracker,
    private val batteryTracker: BatteryPowerTracker
) {

    fun getSystemCounters(): Map<SystemCounterType, Int> {
        return mapOf(
            SystemCounterType.STEPS to activityTracker.getSteps(),
            SystemCounterType.DISTANCE to activityTracker.getDistance(),
            SystemCounterType.FLOORS to activityTracker.getFloors(),
            SystemCounterType.ACTIVE_MINUTES to activityTracker.getActiveMinutes(),
            SystemCounterType.CALORIES to activityTracker.getCalories(),
            SystemCounterType.SCREEN_TIME to deviceUsageTracker.getScreenTime(),
            SystemCounterType.PHONE_UNLOCKS to deviceUsageTracker.getPhoneUnlocks(),
            SystemCounterType.NOTIFICATIONS_RECEIVED to deviceUsageTracker.getNotificationsReceived(),
            SystemCounterType.NOTIFICATIONS_CLEARED to deviceUsageTracker.getNotificationsCleared(),
            SystemCounterType.CALLS_MADE to communicationTracker.getCallsMade(),
            SystemCounterType.CALLS_RECEIVED to communicationTracker.getCallsReceived(),
            SystemCounterType.SMS_RECEIVED to communicationTracker.getSMSReceived(),
            SystemCounterType.PHOTOS_TAKEN to mediaStorageTracker.getPhotosCount(),
            SystemCounterType.VIDEOS_TAKEN to mediaStorageTracker.getVideosCount(),
            SystemCounterType.FILES_DOWNLOADED to mediaStorageTracker.getFilesDownloaded(),
            SystemCounterType.WIFI_CONNECTIONS to networkConnectivityTracker.getWiFiConnections(),
            SystemCounterType.BLUETOOTH_CONNECTIONS to networkConnectivityTracker.getBluetoothConnections(),
            SystemCounterType.MOBILE_DATA_USAGE to networkConnectivityTracker.getMobileDataUsage(),
            SystemCounterType.BATTERY_CHARGES to batteryTracker.getChargeCount(),
            SystemCounterType.DEVICE_RESTARTS to batteryTracker.getDeviceRestarts()
        )
    }
}