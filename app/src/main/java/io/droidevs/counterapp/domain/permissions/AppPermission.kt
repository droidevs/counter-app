package io.droidevs.counterapp.domain.permissions

/**
 * App-level abstraction over Android runtime permissions.
 *
 * NOTE: This is intentionally not 1:1 with every Manifest permission; it's what the app cares about.
 */
enum class AppPermission {
    ReadPhoneState,
    ReceiveSms,
    BluetoothConnect
}

