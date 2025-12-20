package io.droidevs.counterapp.domain.system

enum class SystemCounterType {
    // Activity / Health
    STEPS,
    DISTANCE,
    FLOORS,
    ACTIVE_MINUTES,
    CALORIES,

    // Device Usage
    SCREEN_TIME,
    PHONE_UNLOCKS,
    NOTIFICATIONS_RECEIVED,
    NOTIFICATIONS_CLEARED,

    // Communication
    CALLS_MADE,
    CALLS_RECEIVED,
    SMS_SENT,
    SMS_RECEIVED,

    // Media / Storage
    PHOTOS_TAKEN,
    VIDEOS_TAKEN,
    FILES_DOWNLOADED,

    // Network / Connectivity
    WIFI_CONNECTIONS,
    BLUETOOTH_CONNECTIONS,
    MOBILE_DATA_USAGE,

    // Battery / Power
    BATTERY_CHARGES,
    DEVICE_RESTARTS,

    // System Events
    ALARMS_SET,
    CALENDAR_EVENTS
}
