package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.permissions.AppPermission
import io.droidevs.counterapp.domain.permissions.PermissionError
import io.droidevs.counterapp.domain.permissions.PermissionStatus
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.recoverWith
import io.droidevs.counterapp.domain.result.resultSuspend
import io.droidevs.counterapp.domain.usecases.permissions.PermissionUseCases
import io.droidevs.counterapp.ui.vm.actions.PermissionAction
import io.droidevs.counterapp.ui.vm.events.PermissionEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    private val permissionUseCases: PermissionUseCases
) : ViewModel() {

    private val _event = MutableSharedFlow<PermissionEvent>(extraBufferCapacity = 1)
    val event: SharedFlow<PermissionEvent> = _event.asSharedFlow()

    private val _missing = MutableStateFlow<List<AppPermission>>(emptyList())
    val missing: StateFlow<List<AppPermission>> = _missing.asStateFlow()

    fun onAction(action: PermissionAction) {
        when (action) {
            PermissionAction.EnterSystemCategories -> {
                viewModelScope.launch {
                    // Entry point is Result-driven; we intentionally ignore the value here
                    // because events are emitted for the UI to react to.
                    ensureSystemCategoriesPermissions()
                }
            }

            is PermissionAction.PermissionsResult -> onPermissionsResult(action.grants)

            PermissionAction.RefreshSystemPermissions -> refreshSystemPermissions()
        }
    }

    /**
     * Single Result-driven entry point used when entering System Categories.
     *
     * Contract:
     * - Success(Unit): no permanently denied permissions.
     * - Failure(PermissionError): unexpected internal validation failure.
     *
     * Side-effect:
     * - Emits [PermissionEvent.RequestPermissions] when there are missing permissions we can still ask.
     * - Emits [PermissionEvent.ShowPermanentlyDeniedDialog] when user permanently denied one/more.
     */
    suspend fun ensureSystemCategoriesPermissions(): Result<Unit, PermissionError> =
        resultSuspend {
            // Validate manifest declarations (safety). If this fails, surface it.
            when (val validate = permissionUseCases.validateSystemManifest()) {
                is Result.Failure -> return@resultSuspend Result.Failure(PermissionError.Internal(validate.error.message))
                is Result.Success -> Unit
            }

            // Missing permissions
            val missingNow = permissionUseCases.getMissingSystemPermissions()
                .recoverWith { Result.Success(emptyList()) }
                .let { (it as Result.Success).data }

            _missing.value = missingNow
            if (missingNow.isEmpty()) return@resultSuspend Result.Success(Unit)

            val permanentlyDenied = missingNow.any {
                permissionUseCases.getStatusWithActivity(it) == PermissionStatus.PermanentlyDenied
            }

            if (permanentlyDenied) {
                _event.tryEmit(PermissionEvent.ShowPermanentlyDeniedDialog(missingNow))
                Result.Success(Unit)
            } else {
                val manifestPerms = missingNow
                    .flatMap { permissionUseCases.getManifestPermissions(it) }
                    .distinct()

                _event.tryEmit(PermissionEvent.RequestPermissions(manifestPerms))
                Result.Success(Unit)
            }
        }

    private fun refreshSystemPermissions() {
        viewModelScope.launch {
            permissionUseCases.getMissingSystemPermissions()
                .recoverWith { Result.Success(emptyList()) }
                .let { result ->
                    if (result is Result.Success) {
                        _missing.value = result.data
                    }
                }
        }
    }

    /**
     * Called by UI when permission request result returns.
     */
    private fun onPermissionsResult(grants: Map<String, Boolean>) {
        refreshSystemPermissions()

        viewModelScope.launch {
            val anyDenied = grants.values.any { granted -> !granted }
            if (!anyDenied) return@launch

            val missingNow = _missing.value
            val permanentlyDenied = missingNow.any {
                permissionUseCases.getStatusWithActivity(it) == PermissionStatus.PermanentlyDenied
            }

            if (permanentlyDenied) {
                _event.tryEmit(PermissionEvent.ShowPermanentlyDeniedDialog(missingNow))
            }
        }
    }
}
