package io.droidevs.counterapp.ui.vm.events

import io.droidevs.counterapp.domain.permissions.AppPermission

sealed interface PermissionEvent {
    data class RequestPermissions(val permissions: List<String>) : PermissionEvent

    /** UI should show a dialog and route user to app settings. */
    data class ShowPermanentlyDeniedDialog(val missing: List<AppPermission>) : PermissionEvent
}

