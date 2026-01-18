package io.droidevs.counterapp.ui.label

import io.droidevs.counterapp.domain.coroutines.annotation.ApplicationScope
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.dataOr
import io.droidevs.counterapp.domain.result.onFailure
import io.droidevs.counterapp.domain.result.recoverWith
import io.droidevs.counterapp.domain.usecases.preference.controle.GetLabelControlUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single responsibility: expose whether category labels are visible for counters.
 *
 * In this app "label" == category chip/name on counter cards/rows.
 * Uses application scope so it stays up-to-date across the whole app.
 */
@Singleton
class LabelControlManager @Inject constructor(
    getLabelControlUseCase: GetLabelControlUseCase,
    @ApplicationScope scope: CoroutineScope,
    errorHandler: LabelControlErrorHandler,
) {
    val enabled: StateFlow<Boolean> = getLabelControlUseCase()
        .onFailure { errorHandler.onError(it) }
        .recoverWith { Result.Success(false) }
        .map { it.dataOr { false } }
        .stateIn(scope, SharingStarted.Eagerly, false)
}

