package io.droidevs.counterapp.domain.usecases.permissions

import io.droidevs.counterapp.domain.permissions.AppPermission
import io.droidevs.counterapp.domain.permissions.PermissionGateway
import javax.inject.Inject

class GetSystemRequiredPermissionsUseCase @Inject constructor(
    private val gateway: PermissionGateway
) {
    operator fun invoke(): List<AppPermission> = gateway.requiredForSystemCategories()
}

