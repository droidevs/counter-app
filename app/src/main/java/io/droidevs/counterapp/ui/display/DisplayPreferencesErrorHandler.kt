package io.droidevs.counterapp.ui.display

import io.droidevs.counterapp.domain.result.errors.PreferenceError

fun interface DisplayPreferencesErrorHandler {
    fun onError(error: PreferenceError)
}

