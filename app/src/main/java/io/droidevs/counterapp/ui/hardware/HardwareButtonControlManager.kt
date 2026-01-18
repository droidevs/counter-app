package io.droidevs.counterapp.ui.hardware

import io.droidevs.counterapp.domain.coroutines.annotation.ApplicationScope
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.dataOr
import io.droidevs.counterapp.domain.result.onFailure
import io.droidevs.counterapp.domain.result.recoverWith
import io.droidevs.counterapp.domain.usecases.preference.controle.GetHardwareButtonControlUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single responsibility: expose whether hardware buttons (volume keys) can control counters.
 *
 * MainActivity is the orchestrator: it gates volume-key dispatching based on [enabled].
 *
 * Notes:
 * - Uses the Application-level CoroutineScope so the preference is observed app-wide.
 * - Fails closed (disabled) when preference read fails.
 */
@Singleton
class HardwareButtonControlManager @Inject constructor(
    getHardwareButtonControlUseCase: GetHardwareButtonControlUseCase,
    @ApplicationScope scope: CoroutineScope,
    errorHandler: HardwareButtonControlErrorHandler,
) {

    /** Emits the latest user preference. Defaults to false if preference read fails. */
    val enabled: StateFlow<Boolean> = getHardwareButtonControlUseCase()
        .onFailure { errorHandler.onError(it) }
        .recoverWith { Result.Success(false) }
        .map { it.dataOr { false } }
        // No extra onStart emission: stateIn's initialValue already provides the default.
        .stateIn(scope, SharingStarted.Eagerly, false)
}
