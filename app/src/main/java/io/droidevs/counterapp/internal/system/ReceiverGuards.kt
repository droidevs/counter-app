package io.droidevs.counterapp.internal.system

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
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
     * Outgoing call broadcast has been restricted/deprecated for years.
     * Keep receiver defensive so it doesn't count "impossible" events.
     */
    fun outgoingCallBroadcastSupported(): Boolean {
        // Very conservative: still allow on older devices.
        // On modern Android, this is often blocked unless you are default dialer.
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
    }
}

