package io.droidevs.counterapp.domain.usecases.permissions

import io.droidevs.counterapp.domain.permissions.AppPermission
import io.droidevs.counterapp.domain.permissions.PermissionGateway
import io.droidevs.counterapp.domain.permissions.PermissionStatus
import javax.inject.Inject

class GetPermissionStatusUseCase @Inject constructor(
    private val gateway: PermissionGateway
) {
    operator fun invoke(permission: AppPermission): PermissionStatus = gateway.status(permission)
}

