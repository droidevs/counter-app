package io.droidevs.counterapp.internal.system

import android.Manifest
import android.app.role.RoleManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Telephony
import androidx.core.content.ContextCompat

/**
 * Central place for runtime/SDK guards for BroadcastReceivers.
 *
 * Goal: avoid counting events that can't realistically be delivered on the
 * current Android version / without runtime permissions.
 */
internal object ReceiverGuards {

    /**
     * READ_PHONE_STATE became runtime permission; without it many telephony broadcasts are unusable.
     */
    fun hasReadPhoneState(context: Context): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED

    /**
     * RECEIVE_SMS is runtime permission; without it SMS_RECEIVED won't be delivered.
     */
    fun hasReceiveSms(context: Context): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED

    /**
     * ACCESS_NETWORK_STATE is required for querying network state.
     */
    fun hasAccessNetworkState(context: Context): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED

    /**
     * Guard for Bluetooth state broadcasts.
     *
     * On Android 12+ you typically need BLUETOOTH_CONNECT permission to get meaningful connection state.
     * Some broadcasts may still arrive, but values can be redacted.
     */
    fun hasBluetoothConnectIfNeeded(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    /**
     * Runtime-safe check used by ConnectivityReceiver.
     */
    fun canCheckNetwork(context: Context): Boolean {
        if (!hasAccessNetworkState(context)) return false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        return cm != null
    }

    /**
     * True if this app is the current default dialer.
     *
     * On modern Android versions, outgoing call broadcast visibility is heavily restricted to the default dialer.
     */
    fun isDefaultDialer(context: Context): Boolean {
        val appContext = context.applicationContext
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = appContext.getSystemService(RoleManager::class.java)
            roleManager?.isRoleHeld(RoleManager.ROLE_DIALER) == true
        } else {
            // Best-effort fallback for older devices.
            try {
                val telecom = appContext.getSystemService(Context.TELECOM_SERVICE) as? android.telecom.TelecomManager
                telecom?.defaultDialerPackage == appContext.packageName
            } catch (_: Throwable) {
                false
            }
        }
    }

    /**
     * True if this app is the current default SMS app.
     *
     * Even with RECEIVE_SMS, some capabilities are restricted unless you are the default SMS app.
     */
    fun isDefaultSmsApp(context: Context): Boolean {
        val appContext = context.applicationContext
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = appContext.getSystemService(RoleManager::class.java)
            roleManager?.isRoleHeld(RoleManager.ROLE_SMS) == true
        } else {
            @Suppress("DEPRECATION")
            Telephony.Sms.getDefaultSmsPackage(appContext) == appContext.packageName
        }
    }

    /**
     * Outgoing call broadcast has been restricted/deprecated for years.
     * Keep receiver defensive so it doesn't count "impossible" events.
     */
    fun outgoingCallBroadcastSupported(context: Context): Boolean {
        // Modern reality: treat it as supported only for the default dialer.
        return isDefaultDialer(context)
    }
}
