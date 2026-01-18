package io.droidevs.counterapp.domain.permissions

import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.InternalError

/**
 * Read-only permission checks that the domain/UI can use.
 *
 * Requesting permissions is a UI concern; this gateway helps with:
 * - mapping app permissions to manifest permissions
 * - checking current status
 * - detecting permanently denied state (needs Activity)
 */
interface PermissionGateway {

    /** Manifest permission strings for this logical permission. */
    fun manifestPermissions(permission: AppPermission): List<String>

    /** Quick check using application context; returns Granted/Denied. */
    fun status(permission: AppPermission): PermissionStatus

    /**
     * Permanent-denied detection is a UI concern (requires Activity).
     * Domain/data should not depend on Android Activity/Context.
     */
    fun requiredForSystemCategories(): List<AppPermission>

    /**
     * Validate that the required permissions are declared in the manifest.
     * Useful for catching misconfig early.
     */
    suspend fun validateManifestDeclarations(): Result<Unit, InternalError>
}
