package io.droidevs.counterapp.ui.system

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.StringRes
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.system.SystemCounterType
import io.droidevs.counterapp.internal.system.ReceiverGuards

sealed class SystemCounterSupportStatus(
    val isSupported: Boolean,
    @StringRes val titleRes: Int,
    @StringRes val messageRes: Int
) {
    class Supported(
        @StringRes titleRes: Int,
        @StringRes messageRes: Int
    ) : SystemCounterSupportStatus(true, titleRes, messageRes)

    class NotSupported(
        @StringRes titleRes: Int,
        @StringRes messageRes: Int
    ) : SystemCounterSupportStatus(false, titleRes, messageRes)

    companion object {

        fun evaluate(context: Context, systemKey: String?): SystemCounterSupportStatus {
            val key = systemKey ?: return Supported(
                R.string.system_counter_working_title,
                R.string.system_counter_working_message
            )

            val type = SystemCounterType.entries.firstOrNull { it.key == key }
                ?: return Supported(R.string.system_counter_working_title, R.string.system_counter_working_message)

            fun granted(permission: String): Boolean =
                ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

            return when (type) {
                // Activity / health counters depend on whatever tracker implementation you ship.
                // We assume "works" at UI level.
                SystemCounterType.STEPS,
                SystemCounterType.DISTANCE,
                SystemCounterType.FLOORS,
                SystemCounterType.ACTIVE_MINUTES,
                SystemCounterType.CALORIES -> Supported(
                    R.string.system_counter_working_title,
                    R.string.system_counter_activity_tracker_working
                )

                // Device usage
                SystemCounterType.SCREEN_TIME -> Supported(
                    R.string.system_counter_working_title,
                    R.string.system_counter_screen_time_working
                )

                SystemCounterType.PHONE_UNLOCKS -> Supported(
                    R.string.system_counter_working_title,
                    R.string.system_counter_unlocks_working
                )

                SystemCounterType.NOTIFICATIONS_RECEIVED,
                SystemCounterType.NOTIFICATIONS_CLEARED -> {
                    val enabled = NotificationManagerCompat.getEnabledListenerPackages(context)
                        .contains(context.packageName)

                    if (enabled) {
                        Supported(
                            R.string.system_counter_working_title,
                            R.string.system_counter_notifications_working
                        )
                    } else {
                        NotSupported(
                            R.string.system_counter_not_working_title,
                            R.string.system_counter_requires_notification_listener
                        )
                    }
                }

                // Communication
                SystemCounterType.CALLS_RECEIVED -> {
                    if (ReceiverGuards.hasReadPhoneState(context)) {
                        Supported(
                            R.string.system_counter_working_title,
                            R.string.system_counter_calls_received_working
                        )
                    } else {
                        NotSupported(
                            R.string.system_counter_not_working_title,
                            R.string.system_counter_requires_read_phone_state
                        )
                    }
                }

                SystemCounterType.CALLS_MADE -> {
                    // NEW_OUTGOING_CALL is increasingly restricted/deprecated.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        NotSupported(
                            R.string.system_counter_not_working_title,
                            R.string.system_counter_outgoing_call_restricted
                        )
                    } else {
                        Supported(
                            R.string.system_counter_working_title,
                            R.string.system_counter_call_made_working_message
                        )
                    }
                }

                SystemCounterType.SMS_RECEIVED -> {
                    if (ReceiverGuards.hasReceiveSms(context)) {
                        Supported(
                            R.string.system_counter_working_title,
                            R.string.system_counter_sms_working_message
                        )
                    } else {
                        NotSupported(
                            R.string.system_counter_not_working_title,
                            R.string.system_counter_requires_receive_sms
                        )
                    }
                }

                SystemCounterType.SMS_SENT -> {
                    if (granted(Manifest.permission.SEND_SMS)) {
                        Supported(
                            R.string.system_counter_working_title,
                            R.string.system_counter_sms_sent_working
                        )
                    } else {
                        NotSupported(
                            R.string.system_counter_not_working_title,
                            R.string.system_counter_requires_send_sms
                        )
                    }
                }

                // Media/storage
                SystemCounterType.PHOTOS_TAKEN,
                SystemCounterType.VIDEOS_TAKEN -> {
                    // READ_MEDIA_* on 33+, READ_EXTERNAL_STORAGE below.
                    val mediaGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        granted(Manifest.permission.READ_MEDIA_IMAGES) || granted(Manifest.permission.READ_MEDIA_VIDEO)
                    } else {
                        granted(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }

                    if (mediaGranted) {
                        Supported(
                            R.string.system_counter_working_title,
                            R.string.system_counter_media_working
                        )
                    } else {
                        NotSupported(
                            R.string.system_counter_not_working_title,
                            R.string.system_counter_requires_media_permission
                        )
                    }
                }

                SystemCounterType.FILES_DOWNLOADED -> Supported(
                    R.string.system_counter_working_title,
                    R.string.system_counter_downloads_working
                )

                // Network/connectivity
                SystemCounterType.WIFI_CONNECTIONS -> {
                    if (ReceiverGuards.canCheckNetwork(context)) {
                        Supported(
                            R.string.system_counter_working_title,
                            R.string.system_counter_wifi_working
                        )
                    } else {
                        NotSupported(
                            R.string.system_counter_not_working_title,
                            R.string.system_counter_requires_access_network_state
                        )
                    }
                }

                SystemCounterType.BLUETOOTH_CONNECTIONS -> {
                    if (ReceiverGuards.hasBluetoothConnectIfNeeded(context)) {
                        Supported(
                            R.string.system_counter_working_title,
                            R.string.system_counter_bluetooth_working
                        )
                    } else {
                        NotSupported(
                            R.string.system_counter_not_working_title,
                            R.string.system_counter_requires_bluetooth_connect
                        )
                    }
                }

                SystemCounterType.MOBILE_DATA_USAGE -> Supported(
                    R.string.system_counter_working_title,
                    R.string.system_counter_mobile_data_working
                )

                // Battery / power
                SystemCounterType.BATTERY_CHARGES -> Supported(
                    R.string.system_counter_working_title,
                    R.string.system_counter_battery_charges_working
                )

                SystemCounterType.DEVICE_RESTARTS -> Supported(
                    R.string.system_counter_working_title,
                    R.string.system_counter_restarts_working
                )

                SystemCounterType.DEVICE_SHUTDOWNS -> Supported(
                    R.string.system_counter_working_title,
                    R.string.system_counter_shutdowns_working
                )

                // System events - not implemented by collectors right now (no broadcast/workers).
                SystemCounterType.ALARMS_SET,
                SystemCounterType.CALENDAR_EVENTS -> NotSupported(
                    R.string.system_counter_not_working_title,
                    R.string.system_counter_not_implemented
                )
            }
        }
    }
}
