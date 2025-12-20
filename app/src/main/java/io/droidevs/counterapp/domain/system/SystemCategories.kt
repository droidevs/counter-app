package io.droidevs.counterapp.domain.system

enum class SystemCategory(
    val displayName: String,
    val color: Int
) {
    ACTIVITY("Activity & Health", 0xFF4CAF50.toInt()),
    DEVICE_USAGE("Device Usage", 0xFFFF9800.toInt()),
    COMMUNICATION("Communication", 0xFF2196F3.toInt()),
    MEDIA_STORAGE("Media & Storage", 0xFFE91E63.toInt()),
    NETWORK_CONNECTIVITY("Network & Connectivity", 0xFF9C27B0.toInt()),
    BATTERY_POWER("Battery & Power", 0xFF795548.toInt()),
    SYSTEM_EVENTS("System Events", 0xFF607D8B.toInt());

    val systemCounterCount: Int
        get() = SystemCounterType.entries.toTypedArray().count { it.category == this }
}
