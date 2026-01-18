package io.droidevs.counterapp.domain.usecases.permissions

import io.droidevs.counterapp.domain.permissions.AppPermission
import io.droidevs.counterapp.domain.permissions.PermissionGateway
import javax.inject.Inject

class GetManifestPermissionsUseCase @Inject constructor(
    private val gateway: PermissionGateway
) {
    operator fun invoke(permission: AppPermission): List<String> = gateway.manifestPermissions(permission)
}

