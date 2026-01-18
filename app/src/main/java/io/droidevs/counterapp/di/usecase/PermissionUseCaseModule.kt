package io.droidevs.counterapp.di.usecase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.droidevs.counterapp.domain.permissions.PermissionGateway
import io.droidevs.counterapp.domain.usecases.permissions.GetManifestPermissionsUseCase
import io.droidevs.counterapp.domain.usecases.permissions.GetMissingSystemPermissionsUseCase
import io.droidevs.counterapp.domain.usecases.permissions.GetPermissionStatusUseCase
import io.droidevs.counterapp.domain.usecases.permissions.GetPermissionStatusWithActivityUseCase
import io.droidevs.counterapp.domain.usecases.permissions.GetSystemPermissionsStatusUseCase
import io.droidevs.counterapp.domain.usecases.permissions.GetSystemRequiredPermissionsUseCase
import io.droidevs.counterapp.domain.usecases.permissions.PermissionUseCases
import io.droidevs.counterapp.domain.usecases.permissions.ValidateSystemPermissionsManifestUseCase

@Module
@InstallIn(ViewModelComponent::class)
object PermissionUseCaseModule {

    @Provides
    fun provideGetSystemRequiredPermissionsUseCase(gateway: PermissionGateway): GetSystemRequiredPermissionsUseCase =
        GetSystemRequiredPermissionsUseCase(gateway)

    @Provides
    fun provideGetManifestPermissionsUseCase(gateway: PermissionGateway): GetManifestPermissionsUseCase =
        GetManifestPermissionsUseCase(gateway)

    @Provides
    fun provideGetPermissionStatusUseCase(gateway: PermissionGateway): GetPermissionStatusUseCase =
        GetPermissionStatusUseCase(gateway)

    @Provides
    fun provideGetPermissionStatusWithActivityUseCase(gateway: PermissionGateway): GetPermissionStatusWithActivityUseCase =
        GetPermissionStatusWithActivityUseCase(gateway)

    @Provides
    fun provideGetMissingSystemPermissionsUseCase(gateway: PermissionGateway): GetMissingSystemPermissionsUseCase =
        GetMissingSystemPermissionsUseCase(gateway)

    @Provides
    fun provideGetSystemPermissionsStatusUseCase(gateway: PermissionGateway): GetSystemPermissionsStatusUseCase =
        GetSystemPermissionsStatusUseCase(gateway)

    @Provides
    fun provideValidateSystemPermissionsManifestUseCase(gateway: PermissionGateway): ValidateSystemPermissionsManifestUseCase =
        ValidateSystemPermissionsManifestUseCase(gateway)

    @Provides
    fun providePermissionUseCases(
        getRequiredForSystemCategories: GetSystemRequiredPermissionsUseCase,
        getManifestPermissions: GetManifestPermissionsUseCase,
        getStatus: GetPermissionStatusUseCase,
        getStatusWithActivity: GetPermissionStatusWithActivityUseCase,
        getMissingSystemPermissions: GetMissingSystemPermissionsUseCase,
        getSystemPermissionsStatus: GetSystemPermissionsStatusUseCase,
        validateSystemManifest: ValidateSystemPermissionsManifestUseCase
    ): PermissionUseCases = PermissionUseCases(
        getRequiredForSystemCategories = getRequiredForSystemCategories,
        getManifestPermissions = getManifestPermissions,
        getStatus = getStatus,
        getStatusWithActivity = getStatusWithActivity,
        getMissingSystemPermissions = getMissingSystemPermissions,
        getSystemPermissionsStatus = getSystemPermissionsStatus,
        validateSystemManifest = validateSystemManifest
    )
}

