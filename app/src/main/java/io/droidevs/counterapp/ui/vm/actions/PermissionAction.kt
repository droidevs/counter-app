package io.droidevs.counterapp.ui.vm.actions

/** Centralized permission actions to keep the PermissionViewModel API consistent with the rest of the app. */
sealed interface PermissionAction {
    /** User navigated to the System Categories screen; ensure all required permissions are handled. */
    data object EnterSystemCategories : PermissionAction

    /** UI callback for ActivityResultContracts.RequestMultiplePermissions. */
    data class PermissionsResult(val grants: Map<String, Boolean>) : PermissionAction

    /** Optional: re-check current system permissions. */
    data object RefreshSystemPermissions : PermissionAction
}

