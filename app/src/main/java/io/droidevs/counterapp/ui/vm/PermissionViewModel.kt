package io.droidevs.counterapp.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.droidevs.counterapp.domain.permissions.AppPermission
import io.droidevs.counterapp.domain.permissions.PermissionError
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.recoverWith
import io.droidevs.counterapp.domain.result.resultSuspend
import io.droidevs.counterapp.domain.usecases.permissions.PermissionUseCases
import io.droidevs.counterapp.ui.vm.actions.PermissionAction
import io.droidevs.counterapp.ui.vm.events.PermissionEvent
import io.droidevs.counterapp.util.TracingHelper
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
    private val permissionUseCases: PermissionUseCases,
    private val tracing: TracingHelper
) : ViewModel() {

    private val _event = MutableSharedFlow<PermissionEvent>(extraBufferCapacity = 1)
    val event: SharedFlow<PermissionEvent> = _event.asSharedFlow()

    private val _missing = MutableStateFlow<List<AppPermission>>(emptyList())
    val missing: StateFlow<List<AppPermission>> = _missing.asStateFlow()

    fun onAction(action: PermissionAction) {
        when (action) {
            PermissionAction.EnterSystemCategories -> {
                viewModelScope.launch {
                    tracing.tracedSuspend("permission_enter_system_categories") {
                        ensureSystemCategoriesPermissions()
                    }
                }
            }

            is PermissionAction.PermissionsResult -> onPermissionsResult(action.grants)

            PermissionAction.RefreshSystemPermissions -> refreshSystemPermissions()
        }
    }

    /**
     * Result-driven system permission entry point.
     * This method does NOT attempt to detect permanently-denied (Activity-only concern).
     *
     * Side-effect:
     * - Emits [PermissionEvent.RequestPermissions] when there are missing permissions.
     */
    suspend fun ensureSystemCategoriesPermissions(): Result<Unit, PermissionError> =
        tracing.tracedSuspend("permission_ensure_system") {
            resultSuspend {
                when (val validate = permissionUseCases.validateSystemManifest()) {
                    is Result.Failure -> return@resultSuspend Result.Failure(PermissionError.Internal(validate.error.message))
                    is Result.Success -> Unit
                }

                val missingNow = permissionUseCases.getMissingSystemPermissions()
                    .recoverWith { Result.Success(emptyList()) }
                    .let { (it as Result.Success).data }

                _missing.value = missingNow
                if (missingNow.isEmpty()) return@resultSuspend Result.Success(Unit)

                val manifestPerms = missingNow
                    .flatMap { permissionUseCases.getManifestPermissions(it) }
                    .distinct()

                _event.tryEmit(PermissionEvent.RequestPermissions(manifestPerms))
                Result.Success(Unit)
            }
        }

    private fun refreshSystemPermissions() {
        viewModelScope.launch {
            tracing.tracedSuspend("permission_refresh_system") {
                permissionUseCases.getMissingSystemPermissions()
            }
                .recoverWith { Result.Success(emptyList()) }
                .let { result ->
                    if (result is Result.Success) {
                        _missing.value = result.data
                    }
                }
        }
    }

    private fun onPermissionsResult(grants: Map<String, Boolean>) {
        refreshSystemPermissions()

        viewModelScope.launch {
            tracing.tracedSuspend("permission_on_result") {
                val anyDenied = grants.values.any { granted -> !granted }
                if (!anyDenied) return@tracedSuspend

                // Permanently denied detection is handled in Fragment (has Activity).
                // VM just exposes missing permissions and emits request events.
            }
        }
    }
}
