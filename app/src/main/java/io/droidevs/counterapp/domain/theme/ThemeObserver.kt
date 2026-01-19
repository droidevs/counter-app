package io.droidevs.counterapp.domain.theme

import kotlinx.coroutines.CoroutineScope

/**
 * Observes theme setting changes and applies them.
 *
 * MainActivity is responsible for calling [start] once.
 */
interface ThemeObserver {
    fun start(scope: CoroutineScope)
}

