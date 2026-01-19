package io.droidevs.counterapp.data.theme

import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.dataOr
import io.droidevs.counterapp.domain.theme.ThemeApplier
import io.droidevs.counterapp.domain.theme.ThemeObserver
import io.droidevs.counterapp.domain.usecases.preference.display.GetThemeUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ThemeObserverImpl @Inject constructor(
    private val getThemeUseCase: GetThemeUseCase,
    private val applier: ThemeApplier,
) : ThemeObserver {

    @Volatile
    private var started = false

    override fun start(scope: CoroutineScope) {
        // MainActivity calls this from repeatOnLifecycle; ensure we only start one collector.
        if (started) return
        started = true

        // Apply default (SYSTEM) if preference read fails.
        scope.launch {
            getThemeUseCase()
                .map { res -> res.dataOr { io.droidevs.counterapp.data.Theme.SYSTEM } }
                //.distinctUntilChanged()
                .collect { theme ->
                    applier.apply(theme)
                }
        }
    }
}
