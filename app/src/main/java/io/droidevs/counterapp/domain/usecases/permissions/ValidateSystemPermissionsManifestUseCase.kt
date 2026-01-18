package io.droidevs.counterapp.domain.usecases.permissions

import io.droidevs.counterapp.domain.permissions.PermissionGateway
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.InternalError
import javax.inject.Inject

class ValidateSystemPermissionsManifestUseCase @Inject constructor(
    private val gateway: PermissionGateway
) {
    suspend operator fun invoke(): Result<Unit, InternalError> = gateway.validateManifestDeclarations()
}

