package io.droidevs.counterapp.data.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import io.droidevs.counterapp.domain.permissions.AppPermission
import io.droidevs.counterapp.domain.permissions.PermissionGateway
import io.droidevs.counterapp.domain.permissions.PermissionStatus
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.errors.InternalError
import io.droidevs.counterapp.domain.result.runCatchingResult
import javax.inject.Inject

class AndroidPermissionGateway @Inject constructor(
    @ApplicationContext private val appContext: Context
) : PermissionGateway {

    override fun manifestPermissions(permission: AppPermission): List<String> {
        return when (permission) {
            AppPermission.ReadPhoneState -> listOf(Manifest.permission.READ_PHONE_STATE)
            AppPermission.ReceiveSms -> listOf(Manifest.permission.RECEIVE_SMS)
            AppPermission.BluetoothConnect -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    listOf(Manifest.permission.BLUETOOTH_CONNECT)
                } else {
                    emptyList()
                }
            }
        }
    }

    override fun status(permission: AppPermission): PermissionStatus {
        val perms = manifestPermissions(permission)
        if (perms.isEmpty()) return PermissionStatus.Granted

        val granted = perms.all { p ->
            ContextCompat.checkSelfPermission(appContext, p) == PackageManager.PERMISSION_GRANTED
        }
        return if (granted) PermissionStatus.Granted else PermissionStatus.Denied
    }

    override fun requiredForSystemCategories(): List<AppPermission> {
        val list = mutableListOf(
            AppPermission.ReadPhoneState,
            AppPermission.ReceiveSms
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            list += AppPermission.BluetoothConnect
        }

        return list
    }

    override suspend fun validateManifestDeclarations(): Result<Unit, InternalError> = runCatchingResult(
        errorTransform = { InternalError("Failed to validate manifest permissions") }
    ) {
        val info: PackageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            appContext.packageManager.getPackageInfo(
                appContext.packageName,
                PackageManager.PackageInfoFlags.of(PackageManager.GET_PERMISSIONS.toLong())
            )
        } else {
            @Suppress("DEPRECATION")
            appContext.packageManager.getPackageInfo(appContext.packageName, PackageManager.GET_PERMISSIONS)
        }

        val requested = info.requestedPermissions?.toSet().orEmpty()
        val expected = requiredForSystemCategories().flatMap { manifestPermissions(it) }.toSet()

        val missing = expected.filterNot { requested.contains(it) }
        if (missing.isNotEmpty()) {
            throw IllegalStateException("Missing permissions in manifest: $missing")
        }

        Unit
    }
}
