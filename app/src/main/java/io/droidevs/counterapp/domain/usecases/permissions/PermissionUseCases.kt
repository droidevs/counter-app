package io.droidevs.counterapp.domain.usecases.permissions

import javax.inject.Inject

data class PermissionUseCases @Inject constructor(
    val getRequiredForSystemCategories: GetSystemRequiredPermissionsUseCase,
    val getManifestPermissions: GetManifestPermissionsUseCase,
    val getStatus: GetPermissionStatusUseCase,
    val getMissingSystemPermissions: GetMissingSystemPermissionsUseCase,
    val getSystemPermissionsStatus: GetSystemPermissionsStatusUseCase,
    val validateSystemManifest: ValidateSystemPermissionsManifestUseCase
)

