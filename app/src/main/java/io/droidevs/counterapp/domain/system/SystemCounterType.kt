package io.droidevs.counterapp.domain.system

enum class SystemCounterType(

    /** Stable system key (used in DB & collectors) */
    val key: String,

    /** Human readable name for UI */
    val displayName: String,

    /** Which system category this counter belongs to */
    val category: SystemCategory,

    /** Default starting value */
    val defaultValue: Int = 0,

    /** Unit shown in UI (steps, minutes, times, MB, etc.) */
    val unit: CounterUnit,

    /** How the value is updated */
    val updateMode: UpdateMode,

    /** Is this counter event-based or absolute */
    val nature: CounterNature
) {

    // ───────────── Activity / Health ─────────────
    STEPS(
        key = "STEPS",
        displayName = "Steps",
        category = SystemCategory.ACTIVITY,
        unit = CounterUnit.STEPS,
        updateMode = UpdateMode.ABSOLUTE,
        nature = CounterNature.CUMULATIVE
    ),

    DISTANCE(
        key = "DISTANCE",
        displayName = "Distance",
        category = SystemCategory.ACTIVITY,
        unit = CounterUnit.METERS,
        updateMode = UpdateMode.ABSOLUTE,
        nature = CounterNature.CUMULATIVE
    ),

    FLOORS(
        key = "FLOORS",
        displayName = "Floors Climbed",
        category = SystemCategory.ACTIVITY,
        unit = CounterUnit.FLOORS,
        updateMode = UpdateMode.ABSOLUTE,
        nature = CounterNature.CUMULATIVE
    ),

    ACTIVE_MINUTES(
        key = "ACTIVE_MINUTES",
        displayName = "Active Minutes",
        category = SystemCategory.ACTIVITY,
        unit = CounterUnit.MINUTES,
        updateMode = UpdateMode.ABSOLUTE,
        nature = CounterNature.CUMULATIVE
    ),

    CALORIES(
        key = "CALORIES",
        displayName = "Calories Burned",
        category = SystemCategory.ACTIVITY,
        unit = CounterUnit.KCAL,
        updateMode = UpdateMode.ABSOLUTE,
        nature = CounterNature.CUMULATIVE
    ),

    // ───────────── Device Usage ─────────────
    SCREEN_TIME(
        key = "SCREEN_TIME",
        displayName = "Screen Time",
        category = SystemCategory.DEVICE_USAGE,
        unit = CounterUnit.MINUTES,
        updateMode = UpdateMode.ABSOLUTE,
        nature = CounterNature.CUMULATIVE
    ),

    PHONE_UNLOCKS(
        key = "PHONE_UNLOCKS",
        displayName = "Phone Unlocks",
        category = SystemCategory.DEVICE_USAGE,
        unit = CounterUnit.TIMES,
        updateMode = UpdateMode.INCREMENT,
        nature = CounterNature.EVENT
    ),

    NOTIFICATIONS_RECEIVED(
        key = "NOTIFICATIONS_RECEIVED",
        displayName = "Notifications Received",
        category = SystemCategory.DEVICE_USAGE,
        unit = CounterUnit.TIMES,
        updateMode = UpdateMode.INCREMENT,
        nature = CounterNature.EVENT
    ),

    NOTIFICATIONS_CLEARED(
        key = "NOTIFICATIONS_CLEARED",
        displayName = "Notifications Cleared",
        category = SystemCategory.DEVICE_USAGE,
        unit = CounterUnit.TIMES,
        updateMode = UpdateMode.INCREMENT,
        nature = CounterNature.EVENT
    ),

    // ───────────── Communication ─────────────
    CALLS_MADE(
        key = "CALLS_MADE",
        displayName = "Calls Made",
        category = SystemCategory.COMMUNICATION,
        unit = CounterUnit.TIMES,
        updateMode = UpdateMode.INCREMENT,
        nature = CounterNature.EVENT
    ),

    CALLS_RECEIVED(
        key = "CALLS_RECEIVED",
        displayName = "Calls Received",
        category = SystemCategory.COMMUNICATION,
        unit = CounterUnit.TIMES,
        updateMode = UpdateMode.INCREMENT,
        nature = CounterNature.EVENT
    ),

    SMS_SENT(
        key = "SMS_SENT",
        displayName = "SMS Sent",
        category = SystemCategory.COMMUNICATION,
        unit = CounterUnit.TIMES,
        updateMode = UpdateMode.INCREMENT,
        nature = CounterNature.EVENT
    ),

    SMS_RECEIVED(
        key = "SMS_RECEIVED",
        displayName = "SMS Received",
        category = SystemCategory.COMMUNICATION,
        unit = CounterUnit.TIMES,
        updateMode = UpdateMode.INCREMENT,
        nature = CounterNature.EVENT
    ),

    // ───────────── Media / Storage ─────────────
    PHOTOS_TAKEN(
        key = "PHOTOS_TAKEN",
        displayName = "Photos Taken",
        category = SystemCategory.MEDIA_STORAGE,
        unit = CounterUnit.TIMES,
        updateMode = UpdateMode.INCREMENT,
        nature = CounterNature.EVENT
    ),

    VIDEOS_TAKEN(
        key = "VIDEOS_TAKEN",
        displayName = "Videos Taken",
        category = SystemCategory.MEDIA_STORAGE,
        unit = CounterUnit.TIMES,
        updateMode = UpdateMode.INCREMENT,
        nature = CounterNature.EVENT
    ),

    FILES_DOWNLOADED(
        key = "FILES_DOWNLOADED",
        displayName = "Files Downloaded",
        category = SystemCategory.MEDIA_STORAGE,
        unit = CounterUnit.TIMES,
        updateMode = UpdateMode.INCREMENT,
        nature = CounterNature.EVENT
    ),

    // ───────────── Network / Connectivity ─────────────
    WIFI_CONNECTIONS(
        key = "WIFI_CONNECTIONS",
        displayName = "Wi-Fi Connections",
        category = SystemCategory.NETWORK_CONNECTIVITY,
        unit = CounterUnit.TIMES,
        updateMode = UpdateMode.INCREMENT,
        nature = CounterNature.EVENT
    ),

    BLUETOOTH_CONNECTIONS(
        key = "BLUETOOTH_CONNECTIONS",
        displayName = "Bluetooth Connections",
        category = SystemCategory.NETWORK_CONNECTIVITY,
        unit = CounterUnit.TIMES,
        updateMode = UpdateMode.INCREMENT,
        nature = CounterNature.EVENT
    ),

    MOBILE_DATA_USAGE(
        key = "MOBILE_DATA_USAGE",
        displayName = "Mobile Data Usage",
        category = SystemCategory.NETWORK_CONNECTIVITY,
        unit = CounterUnit.MB,
        updateMode = UpdateMode.ABSOLUTE,
        nature = CounterNature.CUMULATIVE
    ),

    // ───────────── Battery / Power ─────────────
    BATTERY_CHARGES(
        key = "BATTERY_CHARGES",
        displayName = "Battery Charges",
        category = SystemCategory.BATTERY_POWER,
        unit = CounterUnit.TIMES,
        updateMode = UpdateMode.INCREMENT,
        nature = CounterNature.EVENT
    ),

    DEVICE_RESTARTS(
        key = "DEVICE_RESTARTS",
        displayName = "Device Restarts",
        category = SystemCategory.BATTERY_POWER,
        unit = CounterUnit.TIMES,
        updateMode = UpdateMode.INCREMENT,
        nature = CounterNature.EVENT
    ),

    // ───────────── System Events ─────────────
    ALARMS_SET(
        key = "ALARMS_SET",
        displayName = "Alarms Set",
        category = SystemCategory.SYSTEM_EVENTS,
        unit = CounterUnit.TIMES,
        updateMode = UpdateMode.INCREMENT,
        nature = CounterNature.EVENT
    ),

    CALENDAR_EVENTS(
        key = "CALENDAR_EVENTS",
        displayName = "Calendar Events",
        category = SystemCategory.SYSTEM_EVENTS,
        unit = CounterUnit.TIMES,
        updateMode = UpdateMode.INCREMENT,
        nature = CounterNature.EVENT
    );
}

