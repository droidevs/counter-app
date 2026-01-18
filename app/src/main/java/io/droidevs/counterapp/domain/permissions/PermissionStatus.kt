package io.droidevs.counterapp.domain.permissions

sealed class PermissionStatus {
    data object Granted : PermissionStatus()

    /** User hasn't granted yet, but we can ask. */
    data object Denied : PermissionStatus()

    /** User denied and checked "Don't ask again" (or policy/device blocks dialogs). */
    data object PermanentlyDenied : PermissionStatus()
}

