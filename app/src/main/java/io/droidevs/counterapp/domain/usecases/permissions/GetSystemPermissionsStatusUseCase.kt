package io.droidevs.counterapp.domain.usecases.permissions

import io.droidevs.counterapp.domain.permissions.AppPermission
import io.droidevs.counterapp.domain.permissions.PermissionError
import io.droidevs.counterapp.domain.permissions.PermissionGateway
import io.droidevs.counterapp.domain.permissions.PermissionStatus
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.resultSuspend
import javax.inject.Inject

class GetSystemPermissionsStatusUseCase @Inject constructor(
    private val gateway: PermissionGateway
) {
    suspend operator fun invoke(): Result<Map<AppPermission, PermissionStatus>, PermissionError> =
        resultSuspend {
            val required = gateway.requiredForSystemCategories()
            Result.Success(required.associateWith { gateway.status(it) })
        }
}
